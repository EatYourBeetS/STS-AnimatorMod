package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.cardTextParsing.CTContext;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RenderHelpers;
import org.apache.commons.lang3.StringUtils;

public class EYBCardText
{
    private static final CommonImages.Badges BADGES = GR.Common.Images.Badges;
    private static final CommonImages.CardIcons ICONS = GR.Common.Images.Icons;
    private static final ColoredString cs = new ColoredString("", Settings.CREAM_COLOR);
    private static AbstractPlayer player;
    private float badgeAlphaTargetOffset = 1f;
    private float badgeAlphaOffset = -0.2f;

    protected final CTContext context = new CTContext();
    protected final EYBCard card;
    protected String overrideDescription;

    public static CardStrings ProcessCardStrings(CardStrings strings)
    {
        final String placeholder = "<DESCRIPTION>";
        if (StringUtils.isNotEmpty(strings.UPGRADE_DESCRIPTION))
        {
            strings.UPGRADE_DESCRIPTION = strings.UPGRADE_DESCRIPTION.replace("<DESCRIPTION>", strings.DESCRIPTION);
        }

        return strings;
    }

    public EYBCardText(EYBCard card)
    {
        this.card = card;
    }

    public void ForceRefresh()
    {
        if (overrideDescription != null)
        {
            card.rawDescription = overrideDescription;
        }
        else
        {
            card.rawDescription = card.GetRawDescription();
        }

        context.Initialize(card, card.rawDescription);
    }

    public void OverrideDescription(String description, boolean forceRefresh)
    {
        overrideDescription = description;

        if (forceRefresh)
        {
            ForceRefresh();
        }
    }

    public void RenderDescription(SpriteBatch sb)
    {
        player = EYBCard.player;

        if (card.isLocked || !card.isSeen)
        {
            FontHelper.menuBannerFont.getData().setScale(card.drawScale * 1.25f);
            FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", card.current_x, card.current_y,
            0, -200 * Settings.scale * card.drawScale * 0.5f, card.angle, true, Colors.Cream(card.transparency));
            FontHelper.menuBannerFont.getData().setScale(1f);
            return;
        }

        context.Render(sb);

        RenderAttributes(sb);

        final boolean inHand = player != null && player.hand.contains(card);
        if (card.drawScale > 0.3f)
        {
            RenderBadges(sb, inHand);

            ColoredString header = card.GetHeaderText();
            if (header != null)
            {
                BitmapFont font = RenderHelpers.GetSmallTextFont(card, header.text);
                RenderHelpers.WriteOnCard(sb, card, font, header.text, 0, AbstractCard.RAW_H * 0.48f, header.color, true);
                RenderHelpers.ResetFont(font);
            }

            ColoredString bottom = card.GetBottomText();
            if (bottom != null)
            {
                BitmapFont font = RenderHelpers.GetSmallTextFont(card, bottom.text);
                RenderHelpers.WriteOnCard(sb, card, font, bottom.text, 0, -0.47f * AbstractCard.RAW_H, bottom.color, true);
                RenderHelpers.ResetFont(font);
            }
        }
    }

    public void RenderTooltips(SpriteBatch sb)
    {
        if (EYBCardTooltip.CanRenderTooltips() && (AbstractDungeon.player == null || !AbstractDungeon.player.isDraggingCard || Settings.isTouchScreen))
        {
            EYBCardTooltip.QueueTooltips(card);
        }
    }

    protected void RenderAttributes(SpriteBatch sb)
    {
        AbstractAttribute.leftAlign = true;
        AbstractAttribute temp;
        if ((temp = GetInfo1()) != null)
        {
            temp.Render(sb, card);
            AbstractAttribute.leftAlign = false;
        }
        if ((temp = GetInfo2()) != null)
        {
            temp.Render(sb, card);
            AbstractAttribute.leftAlign = false;
        }
        if ((temp = card.GetSpecialInfo()) != null)
        {
            temp.Render(sb, card);
            AbstractAttribute.leftAlign = false;
        }
    }

    private AbstractAttribute GetInfo1()
    {
        return card.type == AbstractCard.CardType.ATTACK ? card.GetDamageInfo() : card.GetBlockInfo();
    }

    private AbstractAttribute GetInfo2()
    {
        return card.type == AbstractCard.CardType.ATTACK ? card.GetBlockInfo() : card.GetDamageInfo();
    }

    protected void RenderBadges(SpriteBatch sb, boolean inHand)
    {
        final float alpha = UpdateBadgeAlpha();

        int offset_y = 0;
        if (AfterLifeMod.IsAdded(card))
        {
            offset_y -= RenderBadge(sb, BADGES.Afterlife.Texture(), offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.DELAYED))
        {
            offset_y -= RenderBadge(sb, BADGES.Delayed.Texture(), offset_y, alpha, null);
        }
        else if (card.isInnate)
        {
            offset_y -= RenderBadge(sb, BADGES.Innate.Texture(), offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.HARMONIC))
        {
            offset_y -= RenderBadge(sb, BADGES.Harmonic.Texture(), offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.LOYAL))
        {
            offset_y -= RenderBadge(sb, BADGES.Loyal.Texture(), offset_y, alpha, null);
        }
        if (card.isEthereal)
        {
            offset_y -= RenderBadge(sb, BADGES.Ethereal.Texture(), offset_y, alpha, null);
        }
        if (card.selfRetain)
        {
            offset_y -= RenderBadge(sb, BADGES.RetainInfinite.Texture(), offset_y, alpha, null);
        }
        else if (card.retain)
        {
            offset_y -= RenderBadge(sb, BADGES.Retain.Texture(), offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.HASTE_INFINITE))
        {
            offset_y -= RenderBadge(sb, BADGES.HasteInfinite.Texture(), offset_y, alpha, null);
        }
        else if (card.hasTag(GR.Enums.CardTags.HASTE))
        {
            offset_y -= RenderBadge(sb, BADGES.Haste.Texture(), offset_y, alpha, null);
        }

        if (card.purgeOnUse || card.hasTag(GR.Enums.CardTags.PURGE))
        {
            offset_y -= RenderBadge(sb, BADGES.Purge.Texture(), offset_y, alpha, null);
        }
        else if (card.exhaust || card.exhaustOnUseOnce)
        {
            offset_y -= RenderBadge(sb, BADGES.Exhaust.Texture(), offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.AUTOPLAY))
        {
            //noinspection UnusedAssignment
            offset_y -= RenderBadge(sb, BADGES.Autoplay.Texture(), offset_y, alpha, null);
        }

        offset_y = 0;

        final boolean showActualScaling = inHand && player.hoveredCard == card && (player.isDraggingCard || player.isHoveringDropZone || player.inSingleTargetMode);
        for (Affinity affinity : Affinity.All())
        {
            int scaling = card.affinities.GetScaling(affinity, false);
            if (scaling > 0)
            {
                if (showActualScaling)
                {
                    final int amount = (int)CombatStats.Affinities.ApplyScaling(affinity, card, 0);
                    cs.SetColor(amount > 0 ? Colors.Green(1) : Colors.Cream(0.75f));
                    cs.text = "+" + amount;
                }
                else
                {
                    cs.SetColor(Colors.Cream(1));
                    cs.text = "x" + scaling;
                }

                offset_y += RenderScaling(sb, affinity.GetPowerIcon(), cs, offset_y, Color.BLACK);//affinity.GetAlternateColor());
            }
        }
    }

    private float RenderScaling(SpriteBatch sb, TextureRegion texture, ColoredString scaling, float y, Color backgroundColor)
    {
        final float alpha = card.transparency;
        final float offset_x = -AbstractCard.RAW_W * 0.4625f;
        final float offset_y = AbstractCard.RAW_H * 0.08f;//+0.28f;
        final BitmapFont font = EYBFontHelper.CardIconFont_Large;

        RenderHelpers.DrawOnCardAuto(sb, card, GR.Common.Images.Panel_Elliptical.Texture(), new Vector2(offset_x, offset_y + y), 24, 32, backgroundColor, alpha * 0.4f, 1);
        RenderHelpers.DrawOnCardAuto(sb, card, texture, new Vector2(offset_x, offset_y + y + 8), 24, 24, Color.WHITE, alpha, 1);

        font.getData().setScale(0.6f * card.drawScale);
        RenderHelpers.WriteOnCard(sb, card, font, scaling.text, offset_x, offset_y + y - 6, scaling.color, true);
        RenderHelpers.ResetFont(font);

        return 36; // y offset
    }

    private float RenderBadge(SpriteBatch sb, Texture texture, float offset_y, float alpha, String text)
    {
        Vector2 offset = new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y);

        RenderHelpers.DrawOnCardAuto(sb, card, texture, offset, 64, 64, Color.WHITE, alpha, 1);

        if (text != null)
        {
            final BitmapFont font = EYBFontHelper.CardIconFont_Large;

            offset = new Vector2(0.5f, 0.425f + offset_y);
            font.getData().setScale(0.5f * card.drawScale);
            RenderHelpers.WriteOnCard(sb, card, font, text, offset.x, offset.y, Settings.CREAM_COLOR, true);
            RenderHelpers.ResetFont(font);
        }

        return 38;
    }

    protected float UpdateBadgeAlpha()
    {
        if (card.isPreview)
        {
            return card.transparency - badgeAlphaOffset;
        }

        if (card.cardsToPreview instanceof EYBCard)
        {
            ((EYBCard) card.cardsToPreview).cardText.badgeAlphaOffset = badgeAlphaOffset;
        }

        if (card.renderTip && !card.isPopup)
        {
            if (badgeAlphaOffset < badgeAlphaTargetOffset)
            {
                badgeAlphaOffset += GR.UI.Delta(0.33f);
                if (badgeAlphaOffset > badgeAlphaTargetOffset)
                {
                    badgeAlphaOffset = badgeAlphaTargetOffset;
                    badgeAlphaTargetOffset = -0.9f;
                }
            }
            else
            {
                badgeAlphaOffset -= GR.UI.Delta(0.5f);
                if (badgeAlphaOffset < badgeAlphaTargetOffset)
                {
                    badgeAlphaOffset = badgeAlphaTargetOffset;
                    badgeAlphaTargetOffset = 0.5f;
                }
            }

            if (card.transparency >= 1 && badgeAlphaOffset > 0)
            {
                return card.transparency - badgeAlphaOffset;
            }
        }
        else
        {
            badgeAlphaOffset = -0.2f;
            badgeAlphaTargetOffset = 0.5f;
        }

        return card.transparency;
    }
}

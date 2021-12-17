package pinacolada.cards.base;

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
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.cardTextParsing.CTContext;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.utilities.PCLColors;
import pinacolada.utilities.PCLGameUtilities;

import static pinacolada.resources.GR.Enums.CardTags.AFTERLIFE;

public class PCLCardText
{
    private static final PCLImages.Badges BADGES = GR.PCL.Images.Badges;
    private static final PCLImages.CardIcons ICONS = GR.PCL.Images.Icons;
    private static final ColoredString cs = new ColoredString("", Settings.CREAM_COLOR);

    private static AbstractPlayer player;
    private float badgeAlphaTargetOffset = 1f;
    private float badgeAlphaOffset = -0.2f;

    protected final CTContext context = new CTContext();
    protected final PCLCard card;
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

    public PCLCardText(PCLCard card)
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
        player = PCLCard.player;

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
                BitmapFont font = pinacolada.utilities.PCLRenderHelpers.GetSmallTextFont(card, header.text);
                pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, card, font, header.text, 0, AbstractCard.RAW_H * 0.48f, header.color, true);
                pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
            }

            ColoredString bottom = card.GetBottomText();
            if (bottom != null)
            {
                BitmapFont font = pinacolada.utilities.PCLRenderHelpers.GetSmallTextFont(card, bottom.text);
                pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, card, font, bottom.text, 0, -0.47f * AbstractCard.RAW_H, bottom.color, true);
                pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
            }
        }
    }

    public void RenderTooltips(SpriteBatch sb)
    {
        if (PCLCardTooltip.CanRenderTooltips() && (AbstractDungeon.player == null || !AbstractDungeon.player.isDraggingCard || Settings.isTouchScreen))
        {
            PCLCardTooltip.QueueTooltips(card);
        }
    }

    protected void RenderAttributes(SpriteBatch sb)
    {
        AbstractAttribute.leftAlign = true;
        AbstractAttribute temp;
        if ((temp = card.GetPrimaryInfo()) != null)
        {
            temp.Render(sb, card);
            AbstractAttribute.leftAlign = false;
        }
        if ((temp = card.GetSecondaryInfo()) != null)
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

    protected void RenderBadges(SpriteBatch sb, boolean inHand)
    {
        final float alpha = UpdateBadgeAlpha();

        int offset_y = 0;
        if (card.hasTag(AFTERLIFE))
        {
            offset_y -= RenderBadge(sb, BADGES.Afterlife.Texture(), PCLColors.COLOR_AFTERLIFE, offset_y, alpha, null);
        }
        if (card.unplayable || PCLGameUtilities.IsUnplayableThisTurn(card))
        {
            offset_y -= RenderBadge(sb, BADGES.Unplayable.Texture(), PCLColors.COLOR_UNPLAYABLE, offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.DELAYED))
        {
            offset_y -= RenderBadge(sb, BADGES.Delayed.Texture(), PCLColors.COLOR_DELAYED, offset_y, alpha, null);
        }
        else if (card.isInnate || card.hasTag(GR.Enums.CardTags.PCL_INNATE))
        {
            offset_y -= RenderBadge(sb, BADGES.Innate.Texture(), PCLColors.COLOR_INNATE, offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.HARMONIC))
        {
            offset_y -= RenderBadge(sb, BADGES.Harmonic.Texture(), PCLColors.COLOR_HARMONIC, offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.LOYAL))
        {
            offset_y -= RenderBadge(sb, BADGES.Loyal.Texture(), PCLColors.COLOR_LOYAL, offset_y, alpha, null);
        }
        if (card.isEthereal)
        {
            offset_y -= RenderBadge(sb, BADGES.Ethereal.Texture(), PCLColors.COLOR_ETHEREAL, offset_y, alpha, null);
        }
        if (card.selfRetain)
        {
            offset_y -= RenderBadge(sb, BADGES.Retain.Texture(), PCLColors.COLOR_RETAIN, offset_y, alpha, null, true);
        }
        else if (card.retain)
        {
            offset_y -= RenderBadge(sb, BADGES.Retain.Texture(), PCLColors.COLOR_RETAIN, offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.HASTE_INFINITE))
        {
            offset_y -= RenderBadge(sb, BADGES.Haste.Texture(), PCLColors.COLOR_HASTE, offset_y, alpha, null, true);
        }
        else if (card.hasTag(GR.Enums.CardTags.HASTE))
        {
            offset_y -= RenderBadge(sb, BADGES.Haste.Texture(), PCLColors.COLOR_HASTE, offset_y, alpha, null);
        }

        if (card.purgeOnUse || card.hasTag(GR.Enums.CardTags.PURGE))
        {
            offset_y -= RenderBadge(sb, BADGES.Purge.Texture(), PCLColors.COLOR_PURGE, offset_y, alpha, null);
        }
        else if (card.exhaust || card.exhaustOnUseOnce)
        {
            offset_y -= RenderBadge(sb, BADGES.Exhaust.Texture(), PCLColors.COLOR_EXHAUST, offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.AUTOPLAY))
        {
            //noinspection UnusedAssignment
            offset_y -= RenderBadge(sb, BADGES.Autoplay.Texture(), PCLColors.COLOR_AUTOPLAY, offset_y, alpha, null);
        }

        // Render card footers
        if (card.isPopup || (player != null && player.masterDeck.contains(card))) {
            offset_y = 0;
            if (card.hasTag(GR.Enums.CardTags.UNIQUE)) {
                offset_y += RenderFooter(sb, card.isPopup ? ICONS.Unique_L.Texture() : ICONS.Unique.Texture(), offset_y, Color.WHITE, null);
            }
            else if (card.cardData.CanToggleFromPopup && (card.upgraded || card.cardData.UnUpgradedCanToggleForms)) {
                offset_y += RenderFooter(sb, card.isPopup ? ICONS.Multiform_L.Texture() : ICONS.Multiform.Texture(), offset_y, card.auxiliaryData.form != 0 ? Color.WHITE : Color.DARK_GRAY, null);
            }
            else if (card.hasTag(GR.Enums.CardTags.EXPANDED) || card.cardData.CanToggleOnUpgrade) {
                offset_y += RenderFooter(sb, card.isPopup ? ICONS.BranchUpgrade_L.Texture() : ICONS.BranchUpgrade.Texture(), offset_y, card.auxiliaryData.form != 0 ? Color.WHITE : Color.DARK_GRAY, null);
            }
        }

        // Render card scaling
        offset_y = 0;

        final boolean showActualScaling = inHand && player != null && player.hoveredCard == card && (player.isDraggingCard || player.isHoveringDropZone || player.inSingleTargetMode);
        for (PCLAffinity affinity : PCLAffinity.All())
        {
            int scaling = card.affinities.GetScaling(affinity, false);
            if (scaling > 0)
            {
                if (showActualScaling)
                {
                    final int amount = (int) PCLCombatStats.MatchingSystem.ApplyScaling(affinity, card, 0);
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
        final float offset_x = -AbstractCard.RAW_W * 0.4695f;
        final float offset_y = AbstractCard.RAW_H * 0.08f;//+0.28f;
        final BitmapFont font = EYBFontHelper.CardIconFont_Small;

        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, GR.PCL.Images.Panel_Elliptical.Texture(), new Vector2(offset_x, offset_y + y), 24, 32, backgroundColor, alpha * 0.4f, 1);
        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, texture, new Vector2(offset_x, offset_y + y + 8), 24, 24, Color.WHITE, alpha, 1);

        font.getData().setScale(0.9f * card.drawScale);
        pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, card, font, scaling.text, offset_x, offset_y + y - 6, scaling.color, true);
        pinacolada.utilities.PCLRenderHelpers.ResetFont(font);

        return 36; // y offset
    }

    private float RenderBadge(SpriteBatch sb, Texture texture, Color color, float offset_y, float alpha, String text) {
        return RenderBadge(sb, texture, color, offset_y, alpha, text, false);
    }

    private float RenderBadge(SpriteBatch sb, Texture texture, Color color, float offset_y, float alpha, String text, boolean isInfinite)
    {
        Vector2 offset = new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y);

        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, GR.PCL.Images.Badges.Base_Badge.Texture(), new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y), 64, 64, color, alpha, 1);
        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, texture, new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y), 64, 64, Color.WHITE, alpha, 1);
        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, GR.PCL.Images.Badges.Base_Border.Texture(), new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y), 64, 64, Color.WHITE, alpha, 1);
        if (isInfinite) {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, GR.PCL.Images.Badges.Base_Infinite.Texture(), new Vector2(AbstractCard.RAW_W * 0.45f, AbstractCard.RAW_H * 0.45f + offset_y), 64, 64, Color.WHITE, alpha, 1);
        }

        if (text != null)
        {
            final BitmapFont font = EYBFontHelper.CardIconFont_Large;

            offset = new Vector2(0.5f, 0.425f + offset_y);
            font.getData().setScale(0.5f * card.drawScale);
            pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, card, font, text, offset.x, offset.y, Settings.CREAM_COLOR, true);
            pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
        }

        return 38;
    }

    private float RenderFooter(SpriteBatch sb, Texture texture, float y, Color iconColor, String text)
    {
        final float offset_x = -AbstractCard.RAW_W * 0.4595f;
        final float offset_y = y - AbstractCard.RAW_H * 0.46f;
        final float alpha = card.transparency;

        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, GR.PCL.Images.Panel_Elliptical.Texture(),  new Vector2(offset_x, offset_y), 40, 40, Color.BLACK, alpha * 0.4f, 0.8f);
        pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, card, texture,  new Vector2(offset_x, offset_y), 40, 40, iconColor, alpha, 0.8f);

        if (text != null)
        {
            final BitmapFont font = EYBFontHelper.CardIconFont_Large;

            font.getData().setScale(0.5f * card.drawScale);
            pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, card, font, text, offset_x, offset_y, Settings.CREAM_COLOR, true);
            pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
        }

        return 38; // y offset
    }

    protected float UpdateBadgeAlpha()
    {
        if (card.isPreview)
        {
            return card.transparency - badgeAlphaOffset;
        }

        if (card.cardsToPreview instanceof PCLCard)
        {
            ((PCLCard) card.cardsToPreview).cardText.badgeAlphaOffset = badgeAlphaOffset;
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

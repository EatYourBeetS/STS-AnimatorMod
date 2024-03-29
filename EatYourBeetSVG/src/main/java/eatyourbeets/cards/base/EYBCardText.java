package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.cardTextParsing.CTContext;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.RenderHelpers;

public class EYBCardText
{
    private static final CommonImages.Badges BADGES = GR.Common.Images.Badges;
    private static final CommonImages.CardIcons ICONS = GR.Common.Images.Icons;
    private float badgeAlphaTargetOffset = 1f;
    private float badgeAlphaOffset = -0.2f;

    protected final CTContext context = new CTContext();
    protected final EYBCard card;
    protected String overrideDescription;

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
        if (card.isLocked || !card.isSeen)
        {
            FontHelper.menuBannerFont.getData().setScale(card.drawScale * 1.25f);
            FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", card.current_x, card.current_y,
            0, -200 * Settings.scale * card.drawScale * 0.5f, card.angle, true, RenderHelpers.CopyColor(card, Settings.CREAM_COLOR));
            FontHelper.menuBannerFont.getData().setScale(1f);
            return;
        }


        context.Render(sb);

        RenderAttributes(sb);

        final boolean inHand = EYBCard.player != null && EYBCard.player.hand.contains(card);
        if (card.drawScale > 0.3f)
        {
            RenderBadges(sb);

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

                float yPos = AbstractCard.RAW_H;
                if (inHand)
                {
                    font.getData().scale(0.175f);
                    yPos *= 0.57f;
                }
                else
                {
                    yPos *= -0.47f;
                }

                RenderHelpers.WriteOnCard(sb, card, font, bottom.text, 0, yPos, bottom.color, true);
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

    protected void RenderBadges(SpriteBatch sb)
    {
        final float alpha = UpdateBadgeAlpha();

        int offset_y = 0;
        if (card.isInnate)
        {
            offset_y -= RenderBadge(sb, BADGES.Innate.Texture(), offset_y, alpha, null);
        }
        if (card.isEthereal)
        {
            offset_y -= RenderBadge(sb, BADGES.Ethereal.Texture(), offset_y, alpha, null);
        }
        if (card.retain || card.selfRetain)
        {
            offset_y -= RenderBadge(sb, BADGES.Retain.Texture(), offset_y, alpha, null);
        }
        if (card.hasTag(GR.Enums.CardTags.HASTE))
        {
            offset_y -= RenderBadge(sb, BADGES.Haste.Texture(), offset_y, alpha, null);
        }
        if (card.purgeOnUse || card.hasTag(GR.Enums.CardTags.PURGE))
        {
            offset_y -= RenderBadge(sb, BADGES.Purge.Texture(), offset_y, alpha, null);
        }
        if (card.exhaust || card.exhaustOnUseOnce)
        {
            //noinspection UnusedAssignment
            offset_y -= RenderBadge(sb, BADGES.Exhaust.Texture(), offset_y, alpha, null);
        }

        offset_y = 0;
        if (card.intellectScaling > 0)
        {
            offset_y += RenderScaling(sb, ICONS.Intellect.Texture(), (int)card.intellectScaling, offset_y, new Color(0.45f, 0.55f, 0.7f, 1f));
        }
        if (card.agilityScaling > 0)
        {
            offset_y += RenderScaling(sb, ICONS.Agility.Texture(), (int)card.agilityScaling, offset_y, new Color(0.45f, 0.7f, 0.55f, 1f));
        }
        if (card.forceScaling > 0)
        {
            //noinspection UnusedAssignment
            offset_y += RenderScaling(sb, ICONS.Force.Texture(), (int)card.forceScaling, offset_y, new Color(0.8f, 0.5f, 0.5f, 1f));
        }
    }

    private float RenderScaling(SpriteBatch sb, Texture texture, int scaling, float y, Color backgroundColor)
    {
        final float offset_x = -AbstractCard.RAW_W * 0.46f;
        final float offset_y = AbstractCard.RAW_H * 0.08f;//+0.28f;
        final BitmapFont font = EYBFontHelper.CardIconFont_Large;

        RenderHelpers.DrawOnCardAuto(sb, card, GR.Common.Images.Circle.Texture(),
                new Vector2(offset_x, offset_y + y),32, 32, backgroundColor, 1, 1);
        RenderHelpers.DrawOnCardAuto(sb, card, texture, new Vector2(offset_x, offset_y + y), 24, 24);

        font.getData().setScale(0.6f * card.drawScale);
        RenderHelpers.WriteOnCard(sb, card, font, "x" + scaling, (offset_x + 6), (offset_y + y - 12), Settings.CREAM_COLOR, true);
        RenderHelpers.ResetFont(font);

        return 35; // y offset
    }
//
//    private float RenderScaling(SpriteBatch sb, Texture texture, int scaling, float y, Color backgroundColor)
//    {
//        final float offset_x = -AbstractCard.RAW_W * 0.46f;
//        final float offset_y = AbstractCard.RAW_H * 0.08f;//+0.28f;
//        final BitmapFont font = EYBFontHelper.CardIconFont_Large;
//
//        RenderHelpers.DrawOnCardAuto(sb, card, GR.Common.Images.Circle.Texture(), new Vector2(offset_x, offset_y + y), 36, 36, backgroundColor, 1, 1);
//        RenderHelpers.DrawOnCardAuto(sb, card, texture, new Vector2(offset_x, offset_y + y), 28, 28);
//
//        font.getData().setScale(0.6f * card.drawScale);
//        RenderHelpers.WriteOnCard(sb, card, font, "x" + scaling, (offset_x + 6), (offset_y + y - 12), Settings.CREAM_COLOR, true);
//        //RenderHelpers.WriteOnCard(sb, card, font, "x" + (int) scaling, (offset_x + 6), (offset_y + y - 12), Settings.CREAM_COLOR, true);
//        RenderHelpers.ResetFont(font);
//
//        return 38; // y offset
//    }

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
                badgeAlphaOffset += Gdx.graphics.getRawDeltaTime() * 0.33f;
                if (badgeAlphaOffset > badgeAlphaTargetOffset)
                {
                    badgeAlphaOffset = badgeAlphaTargetOffset;
                    badgeAlphaTargetOffset = -0.9f;
                }
            }
            else
            {
                badgeAlphaOffset -= Gdx.graphics.getRawDeltaTime() * 0.5f;
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

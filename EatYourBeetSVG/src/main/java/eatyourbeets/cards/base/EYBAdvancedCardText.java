package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.misc.cardTextParsing.CTContext;
import eatyourbeets.misc.cardTextParsing.CTLine;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class EYBAdvancedCardText extends EYBCardText
{
    private static float drawX;
    private static float drawY;
    private static final Color BASE_COLOR;
    private static final float CARD_TIP_PAD;
    private static final float SHADOW_DIST_Y;
    private static final float SHADOW_DIST_X;
    private static final float BOX_EDGE_H;
    private static final float BOX_BODY_H;
    private static final float BOX_W;
    private static float textHeight;
    private static final float TEXT_OFFSET_X;
    private static final float HEADER_OFFSET_Y;
    private static final float ORB_OFFSET_Y;
    private static final float BODY_OFFSET_Y;
    private static final float BODY_TEXT_WIDTH;
    private static final float TIP_DESC_LINE_SPACING;
    private static final float POWER_ICON_OFFSET_X;

    static
    {
        BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);
        CARD_TIP_PAD = 12.0F * Settings.scale;
        SHADOW_DIST_Y = 14.0F * Settings.scale;
        SHADOW_DIST_X = 9.0F * Settings.scale;
        BOX_EDGE_H = 32.0F * Settings.scale;
        BOX_BODY_H = 64.0F * Settings.scale;
        BOX_W = 320.0F * Settings.scale;
        TEXT_OFFSET_X = 22.0F * Settings.scale;
        HEADER_OFFSET_Y = 12.0F * Settings.scale;
        ORB_OFFSET_Y = -8.0F * Settings.scale;
        BODY_OFFSET_Y = -20.0F * Settings.scale;
        BODY_TEXT_WIDTH = 280.0F * Settings.scale;
        TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;
        POWER_ICON_OFFSET_X = 40.0F * Settings.scale;
    }

    protected final static FieldInfo<ArrayList> _keywords = JavaUtilities.GetField("KEYWORDS", TipHelper.class);
    protected final static FieldInfo<ArrayList> _powerTips = JavaUtilities.GetField("POWER_TIPS", TipHelper.class);
    protected final static FieldInfo<Boolean> _renderedTipsThisFrame = JavaUtilities.GetField("renderedTipThisFrame", TipHelper.class);

    protected final static Color textColor = Settings.CREAM_COLOR.cpy();
    protected final ArrayList<CTLine> descLines = new ArrayList<>();
    protected final CTContext context = new CTContext();

    public EYBAdvancedCardText(EYBCard card, CardStrings cardStrings)
    {
        super(card, cardStrings);
    }

    @Override
    public void InitializeDescription()
    {
        descLines.clear();
        card.keywords.clear();
        context.Initialize(card, card.rawDescription);
    }

    @Override
    public void RenderDescription(SpriteBatch sb)
    {
        if (card.isFlipped || card.isSeen && !card.isLocked)
        {
            context.Render(sb);

            boolean leftAlign = true;
            AbstractAttribute temp;

            if ((temp = card.GetDamageInfo()) != null)
            {
                temp.Render(sb, card, leftAlign);
                leftAlign = false;
            }

            if ((temp = card.GetBlockInfo()) != null)
            {
                temp.Render(sb, card, leftAlign);
                leftAlign = false;
            }

            if ((temp = card.GetSpecialInfo()) != null)
            {
                temp.Render(sb, card, leftAlign);
                leftAlign = false;
            }

            RenderBadges(sb);
        }
        else
        {
            FontHelper.menuBannerFont.getData().setScale(card.drawScale * 1.25F);
            FontHelper.renderRotatedText(sb, FontHelper.menuBannerFont, "? ? ?", card.current_x, card.current_y, 0.0F, -200.0F * Settings.scale * card.drawScale / 2.0F, card.angle, true, textColor);
            FontHelper.menuBannerFont.getData().setScale(1.0F);
        }
    }

    @Override
    public void RenderTooltips(SpriteBatch sb)
    {
        if (card.CanRenderTip() && !Settings.hideCards && !_renderedTipsThisFrame.Get(null))
        {
            if (card.isLocked || !card.isSeen || (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && !Settings.isTouchScreen))
            {
                return;
            }

            _keywords.Get(null).clear();
            _powerTips.Get(null).clear();
            _renderedTipsThisFrame.Set(null, true);

            float x = card.current_x;
            if (card.current_x < (float) Settings.WIDTH * 0.75F)
            {
                x += AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD;
            }
            else
            {
                x -= AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD + BOX_W;
            }

            float y = card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H;
            if (context.tooltips.size() >= 4)
            {
                y += (float) (context.tooltips.size() - 1) * 62.0F * Settings.scale;
            }

            for (EYBCardTooltip tooltip : context.tooltips)
            {
                if (tooltip.title.equals("channel"))
                {
                    continue; // Channel has 5 lines of tooltip...
                }

                y -= tooltip.Render(sb, x, y) + BOX_EDGE_H * 3.15F;
            }
        }
    }

    protected void RenderBadges(SpriteBatch sb)
    {
        if (card.isFlipped || card.isLocked || card.transparency <= 0)
        {
            return;
        }

        final CommonImages.Badges badges = GR.Common.Images.Badges;
        final float x = 100;
        final float y = 150;
        int base_Y = 0;

        if (card.isInnate)
        {
            base_Y -= RenderBadge(sb, badges.Innate.Texture(), x, base_Y + y);
        }

        if (card.isEthereal)
        {
            base_Y -= RenderBadge(sb, badges.Ethereal.Texture(), x, base_Y + y);
        }

        if (card.selfRetain)
        {
            base_Y -= RenderBadge(sb, badges.Retain.Texture(), x, base_Y + y);
        }

        if (card.exhaust)
        {
            base_Y -= RenderBadge(sb, badges.Exhaust.Texture(), x, base_Y + y);
        }
    }

    private float RenderBadge(SpriteBatch sb, Texture texture, float x, float y)
    {
        Vector2 offset = new Vector2(x, y);

        offset.rotate(card.angle);
        offset.scl(card.drawScale * Settings.scale);

        Color color = Color.WHITE.cpy();
        color.a = card.transparency * 0.8f;

        RenderHelpers.DrawOnCard(sb, card, color, texture, card.current_x + offset.x, card.current_y + offset.y, 64, 64);

        return 36;
    }
}

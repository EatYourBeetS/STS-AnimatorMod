package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.misc.cardTextParsing.CTContext;
import eatyourbeets.misc.cardTextParsing.CTLine;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class EYBAdvancedCardText extends EYBCardText
{
    private static final float CARD_TIP_PAD = 12.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale;

    private static final CommonImages.Badges BADGES = GR.Common.Images.Badges;
    private static final CommonImages.CardIcons ICONS = GR.Common.Images.Icons;

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
        if (card.isFlipped || card.isLocked || card.transparency <= 0 || card.drawScale < 0.3f)
        {
            return;
        }

        int offset_y = 0;
        if (card.isInnate)
        {
            offset_y -= RenderBadge(sb, BADGES.Innate.Texture(), offset_y);
        }
        if (card.isEthereal)
        {
            offset_y -= RenderBadge(sb, BADGES.Ethereal.Texture(), offset_y);
        }
        if (card.selfRetain)
        {
            offset_y -= RenderBadge(sb, BADGES.Retain.Texture(), offset_y);
        }
        if (card.exhaust)
        {
            offset_y -= RenderBadge(sb, BADGES.Exhaust.Texture(), offset_y);
        }

        offset_y = 0;
        if (card.forceScaling > 0)
        {
            offset_y -= RenderScaling(sb, ICONS.Force.Texture(), card.forceScaling, offset_y);
        }
        if (card.agilityScaling > 0)
        {
            offset_y -= RenderScaling(sb, ICONS.Agility.Texture(), card.agilityScaling, offset_y);
        }
        if (card.intellectScaling > 0)
        {
            offset_y -= RenderScaling(sb, ICONS.Intellect.Texture(), card.intellectScaling, offset_y);
        }

        ColoredString bottomText = null;
        if (card instanceof MartialArtist)
        {
            bottomText = new ColoredString("Martial Artist", new Color(0.9f, 1f, 0.9f, card.transparency));
        }
        else if (card instanceof Spellcaster)
        {
            bottomText = new ColoredString("Spellcaster", new Color(0.9f, 0.9f, 1.0f, card.transparency));
        }

        if (bottomText != null && card.angle == 0)
        {
            FontHelper.cardTitleFont_small.getData().setScale(0.8f * card.drawScale);
            FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_small, bottomText.text,
            card.current_x, //- (card.drawScale * 0.5f * AbstractCard.IMG_WIDTH),
            card.current_y - (card.drawScale * 0.47f * AbstractCard.IMG_HEIGHT), bottomText.color);
            FontHelper.cardTitleFont_small.getData().setScale(1);
        }
    }

    private float RenderScaling(SpriteBatch sb, Texture texture, float scaling, float y)
    {
        final float scale = card.drawScale * Settings.scale;
        final float offset_x = -0.52f * AbstractCard.RAW_W;
        final float offset_y = 0.22f * AbstractCard.RAW_H;
        final BitmapFont font = FontHelper.cardTitleFont_small;

        RenderHelpers.DrawOnCard(sb, card, texture, new Vector2(offset_x, offset_y + y), 38);

        font.getData().setScale(0.9f * card.drawScale);
        RenderHelpers.WriteOnCard(sb, card, font, "x" + (int)scaling,(offset_x + 19) * scale, (offset_y + y) * scale, RenderHelpers.CopyColor(card, Settings.CREAM_COLOR));
        font.getData().setScale(1);

        return 40;
    }

    private float RenderBadge(SpriteBatch sb, Texture texture, float offset_y)
    {
        final Vector2 offset = new Vector2(100, 140 + offset_y);

        offset.rotate(card.angle);
        offset.scl(card.drawScale * Settings.scale);

        Color color = Color.WHITE.cpy();
        color.a = card.transparency * 0.92f;

        RenderHelpers.DrawOnCard(sb, card, color, texture, card.current_x + offset.x, card.current_y + offset.y, 64, 64);

        return 48;
    }
}

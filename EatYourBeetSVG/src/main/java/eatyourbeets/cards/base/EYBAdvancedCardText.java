package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.misc.cardTextParsing.CTContext;
import eatyourbeets.misc.cardTextParsing.CTLine;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;
import eatyourbeets.utilities.Testing;

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
        if (card.isSeen && !card.isLocked)
        {
            context.Render(sb);

            RenderBlock(sb, !RenderDamage(sb, true));
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

    private boolean RenderDamage(SpriteBatch sb, boolean leftAlign)
    {
        if (card.baseDamage <= 0)
        {
            return false;
        }

        String text;
        Color textColor;
        if (card.isDamageModified)
        {
            textColor = (card.damage >= card.baseDamage) ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
            text = Integer.toString(card.damage);
        }
        else
        {
            textColor = Settings.CREAM_COLOR;
            text = Integer.toString(card.baseDamage);
        }

        String extraText = null;
        if (card.IsMultiDamage())
        {
            extraText = "AoE";
        }

        if (card instanceof Spellcaster)
        {
            Render(sb, GR.Common.Images.ElementalDamage.Texture(), text, textColor, leftAlign, extraText);
        }
        else if (card.hasTag(GR.Enums.CardTags.PIERCING) && card instanceof MartialArtist)
        {
            Render(sb, GR.Common.Images.DealDamagePiercing.Texture(), text, textColor, leftAlign, extraText);
        }
        else if (card.hasTag(GR.Enums.CardTags.PIERCING))
        {
            Render(sb, GR.Common.Images.DealDamageRanged.Texture(), text, textColor, leftAlign, extraText);
        }
        else
        {
            Render(sb, GR.Common.Images.DealDamage.Texture(), text, textColor, leftAlign, extraText);
        }

        return true;
    }

    private boolean RenderBlock(SpriteBatch sb, boolean leftAlign)
    {
        if (card.baseBlock <= 0)
        {
            return false;
        }

        String text;
        Color textColor;
        if (card.isBlockModified)
        {
            textColor = (card.block >= card.baseBlock) ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
            text = Integer.toString(card.block);
        }
        else
        {
            textColor = Settings.CREAM_COLOR;
            text = Integer.toString(card.baseBlock);
        }

        Render(sb, GR.Common.Images.GainBlock.Texture(), text, textColor, leftAlign, null);

        return true;
    }

    private void Render(SpriteBatch sb, Texture icon, String text, Color textColor, boolean leftAlign, String extraText)
    {
        BitmapFont font = DEFAULT_FONT;
        font.getData().setScale(card.drawScale);

        final float scale = Settings.scale * card.drawScale;

        if (leftAlign)
        {
            float base_x = card.current_x - (DESC_OFFSET_X * card.drawScale);
            float base_y = card.current_y - (DESC_OFFSET_Y * card.drawScale);

            RenderHelpers.DrawOnCard(sb, card, icon, base_x, base_y, 48);

            layout.setText(font, text);
            FontHelper.renderFont(sb, font, text, base_x + 42 * scale, base_y + 20 * scale + layout.height/2f, textColor);

//            float width = layout.width;
//            font.getData().setScale(card.drawScale * 0.6f);
//            layout.setText(font, "x2");
//            FontHelper.renderFont(sb, font, "x2", base_x + 42 * scale + width, base_y + 14 * scale + layout.height/2f, textColor);

            //FontHelper.renderFontLeft(sb, font, text, base_x + 42 * scale, base_y + 20 * scale, textColor);

            if (extraText != null)
            {
                DEFAULT_FONT_SMALL.getData().setScale(card.drawScale);
                FontHelper.renderFontLeft(sb, DEFAULT_FONT_SMALL, extraText, base_x + 10 * scale, base_y + 8 * scale, Settings.CREAM_COLOR);
                DEFAULT_FONT_SMALL.getData().setScale(1);
            }
        }
        else
        {
            float base_x = card.current_x + (DESC_OFFSET_X * card.drawScale) - (48 * scale);
            float base_y = card.current_y - (DESC_OFFSET_Y * card.drawScale);

            RenderHelpers.DrawOnCard(sb, card, icon, base_x, base_y, 48);
            FontHelper.renderFontRightAligned(sb, font, text, base_x + 10 * scale, base_y + 20 * scale, textColor);

            if (extraText != null)
            {
                DEFAULT_FONT_SMALL.getData().setScale(card.drawScale);
                FontHelper.renderFontLeft(sb, DEFAULT_FONT_SMALL, extraText, base_x + 10 * scale, base_y + 8 * scale, Settings.CREAM_COLOR);
                DEFAULT_FONT_SMALL.getData().setScale(1);
            }
        }

        font.getData().setScale(1);
    }

    protected final static float DESC_OFFSET_X = (AbstractCard.IMG_WIDTH * 0.5f);
    protected final static float DESC_OFFSET_Y = (AbstractCard.IMG_HEIGHT * 0.10f);
    protected final static BitmapFont DEFAULT_FONT = Testing.GenerateCardStatsFont(38, 2.25f);
    protected final static BitmapFont DEFAULT_FONT_SMALL = Testing.GenerateCardStatsFont(16, 1f);
    protected static final GlyphLayout layout = new GlyphLayout();
}

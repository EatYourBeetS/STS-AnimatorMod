package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class EYBCardTooltip
{
    private final static ArrayList EMPTY_LIST = new ArrayList();

    private final static FieldInfo<String> _body = JUtils.GetField("BODY", TipHelper.class);
    private final static FieldInfo<String> _header = JUtils.GetField("HEADER", TipHelper.class);
    private final static FieldInfo<ArrayList> _card = JUtils.GetField("card", TipHelper.class);
    private final static FieldInfo<ArrayList> _keywords = JUtils.GetField("KEYWORDS", TipHelper.class);
    private final static FieldInfo<ArrayList> _powerTips = JUtils.GetField("POWER_TIPS", TipHelper.class);
    private final static FieldInfo<Boolean> _renderedTipsThisFrame = JUtils.GetField("renderedTipThisFrame", TipHelper.class);

    private static final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>();
    private static final float CARD_TIP_PAD = 12f * Settings.scale;
    private static final float BOX_EDGE_H = 32f * Settings.scale;
    private static final Color BASE_COLOR = new Color(1f, 0.9725f, 0.8745f, 1f);
    private static final float SHADOW_DIST_Y = 14f * Settings.scale;
    private static final float SHADOW_DIST_X = 9f * Settings.scale;
    private static final float BOX_BODY_H = 64f * Settings.scale;
    private static final float TEXT_OFFSET_X = 22f * Settings.scale;
    private static final float HEADER_OFFSET_Y = 12f * Settings.scale;
    private static final float ORB_OFFSET_Y = -8f * Settings.scale;
    private static final float BODY_OFFSET_Y = -20f * Settings.scale;
    private static final float BOX_W = 360f * Settings.scale;
    private static final float BODY_TEXT_WIDTH = 320f * Settings.scale;
    private static final float TIP_DESC_LINE_SPACING = 26f * Settings.scale;
    private static final float POWER_ICON_OFFSET_X = 40f * Settings.scale;
    private static EYBCardTooltip translationTooltip;
    private static EYBCard card;

    public TextureRegion icon;
    public String id;
    public String title;
    public String description;
    public float iconMulti_W = 1;
    public float iconMulti_H = 1;
    public boolean canRender = true;

    public EYBCardTooltip(String title, String description)
    {
        this.title = title;
        this.description = description;
    }

    public EYBCardTooltip(Keyword keyword)
    {
        this.title = keyword.PROPER_NAME;
        this.description = keyword.DESCRIPTION;
    }

    public static boolean CanRenderTooltips()
    {
        return !_renderedTipsThisFrame.Get(null);
    }

    public static void QueueTooltips(EYBCard source)
    {
        _body.Set(null, null);
        _header.Set(null, null);
        _card.Set(null, null);
        _keywords.Set(null, EMPTY_LIST);
        _powerTips.Set(null, EMPTY_LIST);
        _renderedTipsThisFrame.Set(null, true);

        card = source;
        GR.UI.AddPostRender(EYBCardTooltip::RenderAll);
    }

    public static void ClearTooltips()
    {
        tooltips.clear();
        _renderedTipsThisFrame.Set(null, true);
    }

    public static void RenderAll(SpriteBatch sb)
    {
        tooltips.clear();
        card.GenerateDynamicTooltips(tooltips);
        for (EYBCardTooltip tooltip : card.tooltips)
        {
            if (tooltip.canRender && !tooltips.contains(tooltip))
            {
                tooltips.add(tooltip);
            }
        }

        float x;
        float y;
        if (card.isPopup)
        {
            x = 0.78f * Settings.WIDTH;
            y = 0.85f * Settings.HEIGHT;
        }
        else
        {
            x = card.current_x;
            if (card.current_x < (float) Settings.WIDTH * 0.7f)
            {
                x += AbstractCard.IMG_WIDTH / 2f + CARD_TIP_PAD;
            }
            else
            {
                x -= AbstractCard.IMG_WIDTH / 2f + CARD_TIP_PAD + BOX_W;
            }

            y = card.current_y - BOX_EDGE_H;
            if (tooltips.size() > 3 && card.current_y < Settings.HEIGHT * 0.5f && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.CARD_REWARD)
            {
                float steps = (tooltips.size() - 3) * 0.4f;
                float multi = 1f - (card.current_y / (Settings.HEIGHT * 0.5f));

                y += AbstractCard.IMG_HEIGHT * (0.5f + JUtils.Round(multi * steps, 3));
            }
            else
            {
                y += AbstractCard.IMG_HEIGHT * 0.5f;
            }
        }

        for (EYBCardTooltip tooltip : tooltips)
        {
            y -= tooltip.Render(sb, x, y) + BOX_EDGE_H * 3.15f;
        }

        EYBCardPreview preview = card.GetCardPreview();
        if (preview != null)
        {
            boolean showUpgrade = SingleCardViewPopup.isViewingUpgrade && (AbstractDungeon.player == null || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD);
            preview.Render(sb, card, card.upgraded || showUpgrade);
        }

        if (GR.IsTranslationSupported(Settings.language) && card.isPopup)
        {
            if (translationTooltip == null)
            {
                translationTooltip = new EYBCardTooltip(GR.Animator.Strings.Misc.LocalizationHelpHeader, GR.Animator.Strings.Misc.LocalizationHelp);
            }

            EYBFontHelper.CardTooltipFont.getData().setScale(0.9f);
            translationTooltip.Render(sb,Settings.WIDTH * 0.025f, Settings.HEIGHT * 0.9f);
            RenderHelpers.ResetFont(EYBFontHelper.CardTooltipFont);
        }
    }

    public float Render(SpriteBatch sb, float x, float y)
    {
        final float textHeight = FontHelper.getSmartHeight(EYBFontHelper.CardTooltipFont, description, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING);
        final float h = StringUtils.isEmpty(description) ? (- 40f * Settings.scale) : (- textHeight - 7f * Settings.scale);

        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, BOX_W, BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);

        if (icon != null)
        {
            // To render it on the right: x + BOX_W - TEXT_OFFSET_X - 28 * Settings.scale
            renderTipEnergy(sb, icon, x + TEXT_OFFSET_X, y + ORB_OFFSET_Y, 28 * iconMulti_W, 28 * iconMulti_H);
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(title), x + TEXT_OFFSET_X * 2.5f, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(title), x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        }

        RenderHelpers.WriteSmartText(sb, EYBFontHelper.CardTooltipFont, description, x + TEXT_OFFSET_X, y + BODY_OFFSET_Y, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);

        return h;
    }

    public EYBCardTooltip SetIconSizeMulti(float w, float h)
    {
        this.iconMulti_W = w;
        this.iconMulti_H = h;

        return this;
    }

    public EYBCardTooltip SetIcon(TextureRegion region)
    {
        this.icon = region;

        return this;
    }

    public EYBCardTooltip SetIcon(TextureRegion region, int div)
    {
        int w = region.getRegionWidth();
        int h = region.getRegionHeight();
        int x = region.getRegionX();
        int y = region.getRegionY();
        int half_div = div / 2;
        this.icon = new TextureRegion(region.getTexture(), x + (w / div), y + (h / div), w - (w / half_div), h - (h / half_div));

        return this;
    }

    public EYBCardTooltip SetIcon(Texture texture, int div)
    {
        int w = texture.getWidth();
        int h = texture.getHeight();
        int half_div = div / 2;
        this.icon = new TextureRegion(texture, w / div, h / div, w - (w / half_div), h - (h / half_div));

        return this;
    }

    public EYBCardTooltip ShowText(boolean value)
    {
        this.canRender = value;

        return this;
    }

    public void renderTipEnergy(SpriteBatch sb, TextureRegion region, float x, float y, float width, float height)
    {
        sb.setColor(Color.WHITE);
        sb.draw(region.getTexture(), x, y, 0f, 0f,
        width, height, Settings.scale, Settings.scale, 0f,
        region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
        region.getRegionHeight(), false, false);
    }

    public String GetTitleOrIcon()
    {
        return (id != null) ? "["+id+"]" : title;
    }

    @Override
    public String toString()
    {
        return GetTitleOrIcon();
    }
}

package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;

public class EYBCardTooltip
{
    private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);
    private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
    private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
    private static final float BOX_EDGE_H = 32.0F * Settings.scale;
    private static final float BOX_BODY_H = 64.0F * Settings.scale;
    private static final float BOX_W = 320.0F * Settings.scale;
    private static final float TEXT_OFFSET_X = 22.0F * Settings.scale;
    private static final float HEADER_OFFSET_Y = 12.0F * Settings.scale;
    private static final float ORB_OFFSET_Y = -8.0F * Settings.scale;
    private static final float BODY_OFFSET_Y = -20.0F * Settings.scale;
    private static final float BODY_TEXT_WIDTH = 280.0F * Settings.scale;
    private static final float TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;
    private static final float POWER_ICON_OFFSET_X = 40.0F * Settings.scale;

    public TextureAtlas.AtlasRegion icon;
    public String title;
    public String description;

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

    public float GetTextHeight()
    {
        return -FontHelper.getSmartHeight(FontHelper.tipBodyFont, description, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 7.0F * Settings.scale;
    }

    public float Render(SpriteBatch sb, float x, float y)
    {
        float h = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, description, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 7.0F * Settings.scale;

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
            renderTipEnergy(sb, icon, x + TEXT_OFFSET_X, y + ORB_OFFSET_Y);

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(title), x + TEXT_OFFSET_X * 2.5F, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(title), x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);
        }

        FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, description, x + TEXT_OFFSET_X, y + BODY_OFFSET_Y, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);

        return h;
    }

    public void renderTipEnergy(SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y)
    {
        sb.setColor(Color.WHITE);
        sb.draw(region.getTexture(), x + region.offsetX * Settings.scale, y + region.offsetY * Settings.scale, 0.0F, 0.0F, (float) region.packedWidth, (float) region.packedHeight, Settings.scale, Settings.scale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
    }
}

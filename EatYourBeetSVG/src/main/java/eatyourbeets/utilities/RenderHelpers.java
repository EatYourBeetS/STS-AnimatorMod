package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardBase;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT3;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_Image;

public class RenderHelpers
{
    private static final StringBuilder builder = new StringBuilder();
    private static final GlyphLayout layout = new GlyphLayout();
    private static final float CARD_ENERGY_IMG_WIDTH = 26.0F * Settings.scale;

    public static void ResetFont(BitmapFont font)
    {
        font.getData().setScale(1);
    }

    public static BitmapFont GetLargeAttributeFont(EYBCard card)
    {
        BitmapFont result;
        if (card.isPopup)
        {
            result = EYBFontHelper.CardIconFont_VeryLarge;
            result.getData().setScale(card.drawScale * 0.5f);
        }
        else
        {
            result = EYBFontHelper.CardIconFont_Large;
            result.getData().setScale(card.drawScale);
        }

        return result;
    }

    public static BitmapFont GetSmallAttributeFont(EYBCard card)
    {
        BitmapFont result;
        if (card.isPopup)
        {
            result = EYBFontHelper.CardIconFont_Large;
            result.getData().setScale(card.drawScale * 0.45f);
        }
        else
        {
            result = EYBFontHelper.CardIconFont_Small;
            result.getData().setScale(card.drawScale * 0.9f);
        }

        return result;
    }

    public static BitmapFont GetSmallTextFont(EYBCardBase card, String text)
    {
        float scaleModifier = 0.8f;
        int length = text.length();
        if (length > 20)
        {
            scaleModifier -= 0.02f * (length - 20);
            if (scaleModifier < 0.5f)
            {
                scaleModifier = 0.5f;
            }
        }

        BitmapFont result;
        if (card.isPopup)
        {
            result = FontHelper.SCP_cardTitleFont_small;
            result.getData().setScale(card.drawScale * scaleModifier * 0.5f);
        }
        else
        {
            result = FontHelper.cardTitleFont_small;
            result.getData().setScale(card.drawScale * scaleModifier);
        }

        return result;
    }

    public static BitmapFont GetDescriptionFont(EYBCardBase card, float scaleModifier)
    {
        BitmapFont result;
        if (card.isPopup)
        {
            result = EYBFontHelper.CardDescriptionFont_Large;
            result.getData().setScale(card.drawScale * scaleModifier * 0.5f);
        }
        else
        {
            result = EYBFontHelper.CardDescriptionFont_Normal;
            result.getData().setScale(card.drawScale * scaleModifier);
        }

        return result;
    }

    public static BitmapFont GetTitleFont(EYBCardBase card)
    {
        BitmapFont result;
        if (card.isPopup)
        {
            result = FontHelper.SCP_cardTitleFont_small;
            result.getData().setScale(card.drawScale * 0.5f);
        }
        else
        {
            result = (card.name.length() > 14) ? FontHelper.cardTitleFont_small : FontHelper.cardTitleFont;
            result.getData().setScale(card.drawScale);
        }

        return result;
    }

    public static BitmapFont GetEnergyFont(EYBCardBase card)
    {
        BitmapFont result;
        if (card.isPopup)
        {
            result = FontHelper.SCP_cardEnergyFont;
            result.getData().setScale(card.drawScale * 0.5f);
        }
        else
        {
            result = FontHelper.cardEnergyFont_L;
            result.getData().setScale(card.drawScale);
        }

        return result;
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - img.originalWidth / 2f, drawY + img.offsetY - img.originalHeight / 2f,
                img.originalWidth / 2f - img.offsetX, img.originalHeight / 2f - img.offsetY,
                img.packedWidth, img.packedHeight, card.drawScale * Settings.scale, card.drawScale * Settings.scale, card.angle);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY)
    {
        int width = img.getWidth();
        int height = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, width, height, false, false);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, TextureRegion img, float drawX, float drawY, float width, float height, float scaleModifier)
    {
        final float scale = card.drawScale * Settings.scale * scaleModifier;

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height, scale, scale, card.angle);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height, float scaleModifier)
    {
        final float scale = card.drawScale * Settings.scale * scaleModifier;

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                scale, scale, card.angle, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY, float width, float height)
    {
        DrawOnCardAuto(sb, card, img, new Vector2(drawX, drawY), width, height, Color.WHITE, card.transparency, 1);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height)
    {
        DrawOnCardAuto(sb, card, img, offset, width, height, Color.WHITE, card.transparency, 1);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height, Color color, float alpha, float scaleModifier)
    {
        if (card.angle != 0)
        {
            offset.rotate(card.angle);
        }

        offset.scl(Settings.scale * card.drawScale);

        DrawOnCardCentered(sb, card, new Color(color.r, color.g, color.b, alpha), img, card.current_x + offset.x, card.current_y + offset.y, width, height, scaleModifier);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, TextureRegion img, Vector2 offset, float width, float height, Color color, float alpha, float scaleModifier)
    {
        if (card.angle != 0)
        {
            offset.rotate(card.angle);
        }

        offset.scl(Settings.scale * card.drawScale);

        DrawOnCardCentered(sb, card, new Color(color.r, color.g, color.b, alpha), img, card.current_x + offset.x, card.current_y + offset.y, width, height, scaleModifier);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY, float size)
    {
        DrawOnCard(sb, card, CopyColor(card, Color.WHITE), img, drawX, drawY, size, size);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY)
    {
        DrawOnCard(sb, card, color, img, drawX, drawY, img.getWidth(), img.getHeight());
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float size)
    {
        DrawOnCard(sb, card, color, img, drawX, drawY, size, size);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height)
    {
        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX, drawY, 0, 0, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, srcWidth, srcHeight, false, false);
    }

    public static void Draw(SpriteBatch sb, Texture img, float drawX, float drawY, float size)
    {
        Draw(sb, img, Color.WHITE, drawX, drawY, size, size);
    }

    public static void Draw(SpriteBatch sb, Texture img, float x, float y, float width, float height)
    {
        Draw(sb, img, Color.WHITE, x, y, width, height);
    }

    public static void Draw(SpriteBatch sb, Texture img, Color color, float x, float y, float width, float height)
    {
        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();

        sb.setColor(color);
        sb.draw(img, x, y, 0, 0, width, height, Settings.scale, Settings.scale, 0, 0, 0,
                srcWidth, srcHeight, false, false);
    }

    public static void WriteOnCard(SpriteBatch sb, AbstractCard card, BitmapFont font, String text, float x, float y, Color color)
    {
        WriteOnCard(sb, card, font, text, x, y, color, false);
    }

    public static void WriteOnCard(SpriteBatch sb, AbstractCard card, BitmapFont font, String text, float x, float y, Color color, boolean roundY)
    {
        final float scale = card.drawScale * Settings.scale;
        color.a = card.transparency;
        FontHelper.renderRotatedText(sb, font, text, card.current_x, card.current_y, x * scale, y * scale, card.angle, roundY, color);
    }

    public static void WriteOnCardAuto(SpriteBatch sb, AbstractCard card, BitmapFont font, String text, Vector2 offset, Color color, boolean roundY)
    {
        if (card.angle != 0)
        {
            offset.rotate(card.angle);
        }

        offset.scl(Settings.scale * card.drawScale);
        color.a = card.transparency;

        FontHelper.renderRotatedText(sb, font, text, card.current_x, card.current_y, offset.x, offset.y, card.angle, roundY, color);
    }

    public static void WriteCentered(SpriteBatch sb, BitmapFont font, String text, Hitbox hb, Color color)
    {
        FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.cY, color);
    }

    public static void WriteCentered(SpriteBatch sb, BitmapFont font, String text, Hitbox hb, Color color, float scale)
    {
        FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.cY, color, scale);
    }

    public static GUI_Image ForTexture(Texture texture)
    {
        return ForTexture(texture, Color.WHITE);
    }

    public static GUI_Image ForTexture(Texture texture, Color color)
    {
        return new GUI_Image(texture, color);
    }

    public static Color CopyColor(AbstractCard card, Color color)
    {
        Color result = color.cpy();
        result.a = card.transparency;
        return result;
    }

    public static ColoredString GetCardAttributeString(EYBCard card, char attributeID)
    {
        switch (attributeID)
        {
            case 'D':
                return card.GetDamageString();
            case 'B':
                return card.GetBlockString();
            case 'M':
                return card.GetMagicNumberString();
            case 'S':
                return card.GetSecondaryValueString();
            default:
                return new ColoredString("?", Settings.RED_TEXT_COLOR);
        }
    }

    private static BitmapFont GenerateFont(BitmapFont source, float size, float borderWidth, float shadowOffset)
    {
        return GenerateFont(source, size, borderWidth, new Color(0f, 0f, 0f, 1f), shadowOffset, new Color(0f, 0f, 0f, 0.5f));
    }

    private static BitmapFont GenerateFont(BitmapFont source, float size, float borderWidth, Color borderColor, float shadowOffset, Color shadowColor)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.hinting = FreeTypeFontGenerator.Hinting.Slight;
        param.spaceX = 0;
        param.kerning = true;
        param.borderColor = borderColor;
        param.borderWidth = borderWidth * Settings.scale;
        param.gamma = 0.9f;
        param.borderGamma = 0.9f;
        param.shadowColor = shadowColor;
        param.shadowOffsetX = Math.round(shadowOffset * Settings.scale);
        param.shadowOffsetY = Math.round(shadowOffset * Settings.scale);
        param.borderStraight = false;
        param.characters = "";
        param.incremental = true;
        param.size = Math.round(size * Settings.scale);
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(source.getData().fontFile); // TitleFontSize.fontFile
        g.scaleForPixelHeight(param.size);
        BitmapFont font = g.generateFont(param);
        font.setUseIntegerPositions(false);
        font.getData().markupEnabled = false;
        if (LocalizedStrings.break_chars != null)
        {
            font.getData().breakChars = LocalizedStrings.break_chars.toCharArray();
        }

        return font;
    }

    public static void WriteSmartText(SpriteBatch sb, BitmapFont font, String text, float x, float y, float lineWidth, float lineSpacing, Color baseColor)
    {
        if (text != null)
        {
            builder.setLength(0);
            layout.setText(font, " ");

            float curWidth = 0.0F;
            float curHeight = 0.0F;
            float spaceWidth = layout.width;

            final FuncT3<Boolean, String, Integer, Character> compare = (s, i, c) -> c == ((i < s.length()) ? s.charAt(i) : null);
            final FuncT1<String, StringBuilder> build = (stringBuilder) ->
            {
                String result = stringBuilder.toString();
                stringBuilder.setLength(0);
                return result;
            };

            Color overrideColor = null;
            boolean foundIcon = false;

            for (int i = 0; i < text.length(); i++)
            {
                char c = text.charAt(i);
                if ('N' == c && compare.Invoke(text, i + 1, 'L'))
                {
                    curWidth = 0.0F;
                    curHeight -= lineSpacing;
                    i += 1;
                }
                else if ('T' == c && compare.Invoke(text, i+1, 'A') && compare.Invoke(text, i+2, 'B'))
                {
                    curWidth += spaceWidth * 5.0F;
                    i += 2;
                }
                else if ('[' == c)
                {
                    foundIcon = true;
                }
                else if (foundIcon && ']' == c)
                {
                    foundIcon = false;
                    TextureRegion icon = GetSmallIcon(build.Invoke(builder));
                    if (icon != null)
                    {
                        final float orbWidth = icon.getRegionWidth();
                        final float orbHeight = icon.getRegionHeight();

                        sb.setColor(new Color(1f, 1f, 1f, baseColor.a));
                        if (curWidth + CARD_ENERGY_IMG_WIDTH > lineWidth)
                        {
                            curHeight -= lineSpacing;
                            sb.draw(icon, x - orbWidth / 2f + 13f * Settings.scale, y + curHeight - orbHeight / 2f - 8f * Settings.scale, orbWidth / 2f, orbHeight / 2f, orbWidth, orbHeight, Settings.scale, Settings.scale, 0f);
                            curWidth = CARD_ENERGY_IMG_WIDTH + spaceWidth;
                        }
                        else
                        {
                            sb.draw(icon, x + curWidth - orbWidth / 2f + 13f * Settings.scale, y + curHeight - orbHeight / 2f - 8f * Settings.scale, orbWidth / 2f, orbHeight / 2f, orbWidth, orbHeight, Settings.scale, Settings.scale, 0f);
                            curWidth += CARD_ENERGY_IMG_WIDTH + spaceWidth;
                        }
                    }
                }
                else if ('#' == c)
                {
                    if (text.length() > i + 1)
                    {
                        overrideColor = GetColor(text.charAt(i+1));
                        i += 1;
                    }
                }
                else if (' ' == c || text.length() == (i+1))
                {
                    if (c != ' ')
                    {
                        builder.append(c);
                    }

                    String word = build.Invoke(builder);
                    if (word != null && word.length() > 0)
                    {
                        if (overrideColor != null)
                        {
                            font.setColor(overrideColor.cpy());
                            overrideColor = null;
                        }
                        else
                        {
                            font.setColor(Color.WHITE.cpy());
                        }

                        layout.setText(font, word);
                        if (curWidth + layout.width > lineWidth)
                        {
                            curHeight -= lineSpacing;
                            font.draw(sb, word, x, y + curHeight);
                            curWidth = layout.width + spaceWidth;
                        }
                        else
                        {
                            font.draw(sb, word, x + curWidth, y + curHeight);
                            curWidth += layout.width + spaceWidth;
                        }
                    }
                }
                else
                {
                    builder.append(c);
                }
            }

            layout.setText(font, text);
        }
    }

    public static TextureAtlas.AtlasRegion GetSmallIcon(String id)
    {
        switch (id)
        {
            case "F":
                return GR.Tooltips.Force.icon;
            case "A":
                return GR.Tooltips.Agility.icon;
            case "I":
                return GR.Tooltips.Intellect.icon;

            case "E":
                return AbstractDungeon.player != null ? AbstractDungeon.player.getOrb() : GR.Tooltips.Energy.icon;
            case "CARD":
                return AbstractCard.orb_card;
            case "POTION":
                return AbstractCard.orb_potion;
            case "RELIC":
                return AbstractCard.orb_relic;
            case "SPECIAL":
                return AbstractCard.orb_special;

            default:
                return null;
        }
    }

    private static Color GetColor(Character c)
    {
        switch (c)
        {
            case 'b':
                return Settings.BLUE_TEXT_COLOR;
            case 'g':
                return Settings.GREEN_TEXT_COLOR;
            case 'p':
                return Settings.PURPLE_COLOR;
            case 'r':
                return Settings.RED_TEXT_COLOR;
            case 'y':
                return Settings.GOLD_COLOR;
            default:
                return Color.WHITE;
        }
    }
}

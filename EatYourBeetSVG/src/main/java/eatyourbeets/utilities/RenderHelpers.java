package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardBase;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT3;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_Image;

import java.util.ArrayList;

public class RenderHelpers
{
    public static final float CARD_ENERGY_IMG_WIDTH = 26.0F * Settings.scale;
    private static final StringBuilder builder = new StringBuilder();
    private static final GlyphLayout layout = new GlyphLayout();

    //copied from TipHelper
    private static final float CARD_TIP_PAD = 12.0F * Settings.scale;
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
    //

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

        return GetSmallTextFont(card, scaleModifier);
    }

    public static BitmapFont GetSmallTextFont(EYBCardBase card, float scaleModifier)
    {
        BitmapFont result;
        if (card.isPopup)
        {
            result = EYBFontHelper.CardTitleFont_Large;
            result.getData().setScale(card.drawScale * scaleModifier * 0.5f);
        }
        else
        {
            // NOTE: this was FontHelper.cardTitleFont_small
            result = EYBFontHelper.CardTitleFont_Small;
            result.getData().setScale(card.drawScale * scaleModifier);
        }

        return result;
    }

    public static BitmapFont GetSmallTextFont_Legacy(EYBCardBase card, String text)
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
            // NOTE: this was FontHelper.cardTitleFont_small
            result = EYBFontHelper.CardTitleFont_Small;
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
        final float scale = 1 / (Math.max(13f, card.name.length()) / 13f);
        if (card.isPopup)
        {
            result = EYBFontHelper.CardTitleFont_Large;
            result.getData().setScale(card.drawScale * 0.5f * scale);
        }
        else
        {
            result = EYBFontHelper.CardTitleFont_Normal;
            result.getData().setScale(card.drawScale * scale);
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
        final int width = img.getWidth();
        final int height = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, width, height, false, false);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, TextureRegion img, float drawX, float drawY, float width, float height, float imgScale)
    {
        final float scale = card.drawScale * Settings.scale * imgScale;

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height, scale, scale, card.angle);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height, float imgScale)
    {
        DrawOnCardCentered(sb, card, color, img, drawX, drawY, width, height, imgScale, 0);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height, float imgScale, float imgRotation)
    {
        final float scale = card.drawScale * Settings.scale * imgScale;

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                scale, scale, card.angle + imgRotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Color color, float drawX, float drawY, float width, float height)
    {
        DrawOnCardAuto(sb, card, img, new Vector2(drawX, drawY), width, height, color, color.a, 1, 0);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY, float width, float height)
    {
        DrawOnCardAuto(sb, card, img, new Vector2(drawX, drawY), width, height, Color.WHITE, card.transparency, 1, 0);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height)
    {
        DrawOnCardAuto(sb, card, img, offset, width, height, Color.WHITE, card.transparency, 1, 0);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, AdvancedTexture img, float drawX, float drawY, float width, float height)
    {
        DrawOnCardAuto(sb, card, img.texture, new Vector2(drawX, drawY), width, height, img.color, img.color.a * card.transparency, 1, 0);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, AdvancedTexture img, Vector2 offset, float width, float height)
    {
        DrawOnCardAuto(sb, card, img.texture, offset, width, height, img.color, img.color.a * card.transparency, 1, 0);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height, Color color, float alpha, float imgScale)
    {
        DrawOnCardAuto(sb, card, img, offset, width, height, color, alpha, imgScale, 0f);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, TextureRegion img, Vector2 offset, float width, float height, Color color, float alpha, float imgScale)
    {
        if (card.angle != 0)
        {
            offset.rotate(card.angle);
        }

        offset.scl(Settings.scale * card.drawScale);

        DrawOnCardCentered(sb, card, new Color(color.r, color.g, color.b, alpha), img, card.current_x + offset.x, card.current_y + offset.y, width, height, imgScale);
    }

    public static void DrawOnCardAuto(SpriteBatch sb, AbstractCard card, Texture img, Vector2 offset, float width, float height, Color color, float alpha, float imgScale, float imgRotation)
    {
        if (card.angle != 0)
        {
            offset.rotate(card.angle);
        }

        offset.scl(Settings.scale * card.drawScale);

        DrawOnCardCentered(sb, card, new Color(color.r, color.g, color.b, alpha), img, card.current_x + offset.x, card.current_y + offset.y, width, height, imgScale, imgRotation);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY, float size)
    {
        DrawOnCard(sb, card, Colors.White(card.transparency), img, drawX, drawY, size, size);
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
        final int srcWidth = img.getWidth();
        final int srcHeight = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX, drawY, 0, 0, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, srcWidth, srcHeight, false, false);
    }

    public static void DrawCentered(SpriteBatch sb, Color color, Texture img, float drawX, float drawY, float width, float height, float imgScale, float imgRotation)
    {
        DrawCentered(sb, color, img, drawX, drawY, width, height, imgScale, imgRotation, false, false);
    }

    public static void DrawCentered(SpriteBatch sb, Color color, Texture img, float drawX, float drawY, float width, float height, float imgScale, float imgRotation, boolean flipX, boolean flipY)
    {
        final float scale = Settings.scale * imgScale;

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                scale, scale, imgRotation, 0, 0, img.getWidth(), img.getHeight(), flipX, flipY);
    }

    public static void DrawCentered(SpriteBatch sb, Color color, TextureRegion img, float drawX, float drawY, float width, float height, float imgScale, float imgRotation)
    {
        final float scale = Settings.scale * imgScale;

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height, scale, scale, imgRotation);
    }

    public static void DrawCentered(SpriteBatch sb, Color color, TextureRegion img, float drawX, float drawY, float width, float height, float imgScale, float imgRotation, boolean flipX, boolean flipY)
    {
        final float scale = Settings.scale * imgScale;

        img.flip(flipX, flipY);
        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height, scale, scale, imgRotation);
        img.flip(flipX, flipY);
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
        final int srcWidth = img.getWidth();
        final int srcHeight = img.getHeight();

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

        color = Colors.Copy(color, color.a * card.transparency);
        FontHelper.renderRotatedText(sb, font, text, card.current_x, card.current_y, x * scale, y * scale, card.angle, roundY, color);
    }

    public static void WriteCentered(SpriteBatch sb, BitmapFont font, String text, float cX, float cY, Color color)
    {
        FontHelper.renderFontCentered(sb, font, text, cX, cY, color);
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
            case 'K':
                return card.GetSpecialVariableString();
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

    public static void WriteSmartText(AbstractPlayer.PlayerClass playerClass, SpriteBatch sb, BitmapFont font, String text, float x, float y, float lineWidth, Color baseColor)
    {
        WriteSmartText(playerClass, sb, font, text, x, y, lineWidth, font.getLineHeight() * Settings.scale, baseColor);
    }

    public static void WriteSmartText(AbstractPlayer.PlayerClass playerClass, SpriteBatch sb, BitmapFont font, String text, float x, float y, float lineWidth, Color baseColor, boolean resizeIcons)
    {
        WriteSmartText(playerClass, sb, font, text, x, y, lineWidth, font.getLineHeight() * Settings.scale, baseColor, resizeIcons);
    }

    public static void WriteSmartText(AbstractPlayer.PlayerClass playerClass, SpriteBatch sb, BitmapFont font, String text, float x, float y, float lineWidth, float lineSpacing, Color baseColor)
    {
        WriteSmartText(playerClass, sb, font, text, x, y, lineWidth, lineSpacing, baseColor, false);
    }

    public static void WriteSmartText(AbstractPlayer.PlayerClass playerClass, SpriteBatch sb, BitmapFont font, String text, float x, float y, float lineWidth, float lineSpacing, Color baseColor, boolean resizeIcons)
    {
        if (text != null)
        {
            builder.setLength(0);
            layout.setText(font, " ");

            float curWidth = 0f;
            float curHeight = 0f;
            float spaceWidth = layout.width;
            final float sizeMultiplier = resizeIcons ? (0.667f + (font.getScaleX() / 3f)) : 1;
            final float imgSize = sizeMultiplier * CARD_ENERGY_IMG_WIDTH;

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
                if (foundIcon)
                {
                    if (']' != c)
                    {
                        builder.append(c);
                        continue;
                    }

                    foundIcon = false;
                    final String id = build.Invoke(builder);
                    final TextureRegion icon = GetSmallIcon(playerClass, id);
                    if (icon == null)
                    {
                        JUtils.LogError(RenderHelpers.class, "Missing icon: " + id);
                        builder.append("MISSING: ").append(id);
                    }
                    else
                    {
                        final float orbWidth = icon.getRegionWidth();
                        final float orbHeight = icon.getRegionHeight();
                        final float scaleX = imgSize / orbWidth;
                        final float scaleY = imgSize / orbHeight;

                        //sb.setColor(baseColor);
                        sb.setColor(1f, 1f, 1f, baseColor.a);
                        if (curWidth + imgSize > lineWidth)
                        {
                            curHeight -= lineSpacing;
                            sb.draw(icon, x - orbWidth / 2f + sizeMultiplier * 13f * Settings.scale, y + curHeight - sizeMultiplier * orbHeight / 2f - 8f * Settings.scale, orbWidth / 2f, orbHeight / 2f, orbWidth, orbHeight, scaleX, scaleY, 0f);
                            curWidth = imgSize + spaceWidth;
                        }
                        else
                        {
                            sb.draw(icon, x + curWidth - orbWidth / 2f + sizeMultiplier * 13f * Settings.scale, y + curHeight - sizeMultiplier * orbHeight / 2f - 8f * Settings.scale, orbWidth / 2f, orbHeight / 2f, orbWidth, orbHeight, scaleX, scaleY, 0f);
                            curWidth += imgSize + spaceWidth;
                        }
                    }
                }
                else if ('N' == c && compare.Invoke(text, i + 1, 'L'))
                {
                    curWidth = 0f;
                    curHeight -= lineSpacing;
                    i += 1;
                }
                else if ('T' == c && compare.Invoke(text, i + 1, 'A') && compare.Invoke(text, i + 2, 'B'))
                {
                    curWidth += spaceWidth * 5.0F;
                    i += 2;
                }
                else if ('[' == c)
                {
                    foundIcon = true;
                }
                else if ('#' == c)
                {
                    if (text.length() > i + 1)
                    {
                        overrideColor = GetColor(text.charAt(i + 1), baseColor);
                        i += 1;
                    }
                }
                else if ('^' == c || ' ' == c || text.length() == (i + 1))
                {
                    if (c != ' ' && c != '^')
                    {
                        builder.append(c);
                    }

                    final String word = build.Invoke(builder);
                    if (word != null && word.length() > 0)
                    {
                        if (overrideColor != null)
                        {
                            font.setColor(overrideColor);
                            overrideColor = null;
                        }
                        else
                        {
                            font.setColor(baseColor);
                        }

                        layout.setText(font, word);
                        if (curWidth + layout.width > lineWidth && !IsPunctuation(word))
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

    public static float GetSmartHeight(AbstractPlayer.PlayerClass playerClass, BitmapFont font, String text, float lineWidth)
    {
        return GetSmartHeight(playerClass, font, text, lineWidth, font.getLineHeight());
    }

    //TODO: Combine with WriteSmartText
    public static float GetSmartHeight(AbstractPlayer.PlayerClass playerClass, BitmapFont font, String text, float lineWidth, float lineSpacing)
    {
        if (text == null || text.isEmpty())
        {
            return 0;
        }

        builder.setLength(0);
        layout.setText(font, " ");

        float curWidth = 0f;
        float curHeight = 0f;
        float spaceWidth = layout.width;

        final FuncT3<Boolean, String, Integer, Character> compare = (s, i, c) -> c == ((i < s.length()) ? s.charAt(i) : null);
        final FuncT1<String, StringBuilder> build = (stringBuilder) ->
        {
            String result = stringBuilder.toString();
            stringBuilder.setLength(0);
            return result;
        };

        boolean foundIcon = false;

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (foundIcon)
            {
                if (']' == c)
                {
                    foundIcon = false;
                    TextureRegion icon = GetSmallIcon(playerClass, build.Invoke(builder));
                    if (icon != null)
                    {
                        if (curWidth + CARD_ENERGY_IMG_WIDTH > lineWidth)
                        {
                            curHeight -= lineSpacing;
                            curWidth = CARD_ENERGY_IMG_WIDTH + spaceWidth;
                        }
                        else
                        {
                            curWidth += CARD_ENERGY_IMG_WIDTH + spaceWidth;
                        }
                    }
                }
                else
                {
                    builder.append(c);
                }
            }
            else if ('[' == c)
            {
                foundIcon = true;
            }
            else if ('N' == c && compare.Invoke(text, i + 1, 'L'))
            {
                curWidth = 0f;
                curHeight -= lineSpacing;
                i += 1;
            }
            else if ('T' == c && compare.Invoke(text, i + 1, 'A') && compare.Invoke(text, i + 2, 'B'))
            {
                curWidth += spaceWidth * 5.0F;
                i += 2;
            }
            else if ('#' == c)
            {
                if (text.length() > i + 1)
                {
                    i += 1;
                }
            }
            else if ('^' == c || ' ' == c || text.length() == (i + 1))
            {
                if (c != '^' && c != ' ')
                {
                    builder.append(c);
                }

                final String word = build.Invoke(builder);
                if (word != null && word.length() > 0)
                {
                    layout.setText(font, word);
                    if (curWidth + layout.width > lineWidth && !IsPunctuation(word))
                    {
                        curHeight -= lineSpacing;
                        curWidth = layout.width + spaceWidth;
                    }
                    else
                    {
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
        return curHeight;
    }

    public static float GetSmartWidth(AbstractPlayer.PlayerClass playerClass, BitmapFont font, String text)
    {
        if (text == null || text.isEmpty())
        {
            return 0;
        }

        builder.setLength(0);
        layout.setText(font, " ");

        float curWidth = 0f;
        float curHeight = 0f;
        float spaceWidth = layout.width;

        final FuncT3<Boolean, String, Integer, Character> compare = (s, i, c) -> c == ((i < s.length()) ? s.charAt(i) : null);
        final FuncT1<String, StringBuilder> build = (stringBuilder) ->
        {
            String result = stringBuilder.toString();
            stringBuilder.setLength(0);
            return result;
        };

        boolean foundIcon = false;

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (foundIcon)
            {
                if (']' == c)
                {
                    foundIcon = false;
                    TextureRegion icon = GetSmallIcon(playerClass, build.Invoke(builder));
                    if (icon != null)
                    {
                        curWidth += CARD_ENERGY_IMG_WIDTH + spaceWidth;
                    }
                }
                else
                {
                    builder.append(c);
                }
            }
            else if ('[' == c)
            {
                foundIcon = true;
            }
            else if ('N' == c && compare.Invoke(text, i + 1, 'L'))
            {
                JUtils.LogError(RenderHelpers.class, "Found 'NL' in GetSmartWidth text.");
                curWidth = 0f;
                i += 1;
            }
            else if ('T' == c && compare.Invoke(text, i + 1, 'A') && compare.Invoke(text, i + 2, 'B'))
            {
                curWidth += spaceWidth * 5.0F;
                i += 2;
            }
            else if ('#' == c)
            {
                if (text.length() > i + 1)
                {
                    i += 1;
                }
            }
            else if ('^' == c || ' ' == c || text.length() == (i + 1))
            {
                if (c != '^' && c != ' ')
                {
                    builder.append(c);
                }

                final String word = build.Invoke(builder);
                if (word != null && word.length() > 0)
                {
                    layout.setText(font, word);
                    curWidth += layout.width + spaceWidth;
                }
            }
            else
            {
                builder.append(c);
            }
        }

        return curWidth;
    }

    public static TextureRegion GetSmallIcon(AbstractPlayer.PlayerClass playerClass, String id)
    {
        if (playerClass == GR.AnimatorClassic.PlayerClass)
        {
            switch (id)
            {
                case "F":
                    return GR.Tooltips.Force.icon;
                case "A":
                    return GR.Tooltips.Agility.icon;
                case "I":
                    return GR.Tooltips.Intellect.icon;
                case "B":
                    return GR.Tooltips.Blessing.icon;
                case "C":
                    return GR.Tooltips.Corruption.icon;
            }
        }

        switch (id)
        {
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
                final EYBCardTooltip tooltip = CardTooltips.FindByID(playerClass, id);
                return (tooltip != null) ? tooltip.icon : null;
        }
    }

    private static Color GetColor(Character c, Color baseColor)
    {
        switch (c)
        {
            case 'b':
                return Settings.BLUE_TEXT_COLOR.cpy();
            case 'g':
                return Settings.GREEN_TEXT_COLOR.cpy();
            case 'p':
                return Settings.PURPLE_COLOR.cpy();
            case 'r':
                return Settings.RED_TEXT_COLOR.cpy();
            case 'y':
                return Settings.GOLD_COLOR.cpy();
            case '#':
                return baseColor.cpy();
            default:
                JUtils.LogWarning(RenderHelpers.class, "Unknown color: #" + c);
                return baseColor.cpy();
        }
    }

    public static float GetTooltipHeight(EYBCardTooltip tip)
    {
        return -GetSmartHeight(tip.playerClass, FontHelper.tipBodyFont, tip.description, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 7.0F * Settings.scale;
    }

    public static float CalculateAdditionalOffset(ArrayList<EYBCardTooltip> tips, float hb_cY)
    {
        return tips.isEmpty() ? 0f : (1f - hb_cY / (float) Settings.HEIGHT) * GetTallestOffset(tips) - (GetTooltipHeight(tips.get(0)) + BOX_EDGE_H * 3.15f) * 0.5f;
    }

    public static float CalculateToAvoidOffscreen(ArrayList<EYBCardTooltip> tips, float hb_cY)
    {
        return tips.isEmpty() ? 0f : Math.max(0.0F, GetTallestOffset(tips) - hb_cY);
    }

    private static float GetTallestOffset(ArrayList<EYBCardTooltip> tips)
    {
        float currentOffset = 0f;
        float maxOffset = 0f;

        for (EYBCardTooltip p : tips)
        {
            float offsetChange = GetTooltipHeight(p) + BOX_EDGE_H * 3.15F;
            if ((currentOffset + offsetChange) >= (float) Settings.HEIGHT * 0.7F)
            {
                currentOffset = 0f;
            }

            currentOffset += offsetChange;
            if (currentOffset > maxOffset)
            {
                maxOffset = currentOffset;
            }
        }

        return maxOffset;
    }

    public static TextureRegion GetCroppedRegion(Texture texture, int div)
    {
        final int w = texture.getWidth();
        final int h = texture.getHeight();
        final int half_div = div / 2;

        return new TextureRegion(texture, w / div, h / div, w - (w / half_div), h - (h / half_div));
    }

    public static TextureRegion GetCroppedRegion(TextureRegion region, int div)
    {
        int w = region.getRegionWidth();
        int h = region.getRegionHeight();
        int x = region.getRegionX();
        int y = region.getRegionY();
        int half_div = div / 2;

        return new TextureRegion(region.getTexture(), x + (w / div), y + (h / div), w - (w / half_div), h - (h / half_div));
    }

    private static boolean IsPunctuation(String word)
    {
        return word != null && word.length() == 1 && ";:,.?!%".contains(word);
    }
}
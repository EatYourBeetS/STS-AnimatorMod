package eatyourbeets.utilities;

import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.TitleFontSize;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Testing
{
    private static int colorIndex = 0;
    private static int fontIndex = 0;
    private static final ArrayList<Field> fonts = new ArrayList<>();
    private static final ArrayList<Field> colors = new ArrayList<>();

    public static BitmapFont GetRandomFont()
    {
        if (fonts.size() == 0)
        {
            for (Field field : FontHelper.class.getDeclaredFields())
            {
                if (field.getType() == BitmapFont.class)
                {
                    field.setAccessible(true);
                    fonts.add(field);
                }
            }
        }

        if (fontIndex >= fonts.size())
        {
            fontIndex = 0;
        }

        Field field = fonts.get(fontIndex++);
        try
        {
            JavaUtilities.Log(Testing.class, field.getName());

            return (BitmapFont) field.get(null);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Color GetRandomColor()
    {
        if (colors.size() == 0)
        {
            for (Field field : Color.class.getDeclaredFields())
            {
                if (field.getType() == Color.class)
                {
                    field.setAccessible(true);
                    colors.add(field);
                }
            }

            for (Field field : Settings.class.getDeclaredFields())
            {
                if (field.getType() == Color.class)
                {
                    field.setAccessible(true);
                    colors.add(field);
                }
            }
        }

        if (colorIndex >= colors.size())
        {
            colorIndex = 0;
        }

        Field field = colors.get(colorIndex++);
        try
        {
            JavaUtilities.Log(Testing.class, field.getName());

            return (Color) field.get(null);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static BitmapFont GenerateFont(float size)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.hinting = FreeTypeFontGenerator.Hinting.Slight;
        param.spaceX = 0;
        param.kerning = true;
        param.borderColor = new Color(0.35F, 0.35F, 0.35F, 1.0F);
        param.borderWidth = 2.25F * Settings.scale;
        param.gamma = 0.9F;
        param.borderGamma = 0.9F;
        param.shadowColor = new Color(0.0F, 0.0F, 0.0F, 0F);
        param.shadowOffsetX = Math.round(3.0F * Settings.scale);
        param.shadowOffsetY = Math.round(3.0F * Settings.scale);
        param.borderStraight = false;
        param.characters = "";
        param.incremental = true;
        param.size = Math.round(size * Settings.scale);
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(TitleFontSize.fontFile);
        g.scaleForPixelHeight(param.size);
        BitmapFont font = g.generateFont(param);
        font.setUseIntegerPositions(false);
        font.getData().markupEnabled = true;
        if (LocalizedStrings.break_chars != null)
        {
            font.getData().breakChars = LocalizedStrings.break_chars.toCharArray();
        }

        return font;
    }

    public static BitmapFont GenerateCardDescFont(float size)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.hinting = FreeTypeFontGenerator.Hinting.Slight;
        param.spaceX = 0;
        param.kerning = true;
        param.borderColor = null; //new Color(0F, 0F, 0F, 1.0F);
        param.borderWidth = 0; //1f * Settings.scale;
        param.gamma = 0.9F;
        param.borderGamma = 0.9F;
        param.shadowColor = new Color(0.0F, 0.0F, 0.0F, 0.25F);
        param.shadowOffsetX = 1;
        param.shadowOffsetY = 1;
        param.borderStraight = false;
        param.characters = "";
        param.incremental = true;
        param.size = Math.round(size * Settings.scale);
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(TitleFontSize.fontFile);
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

    public static BitmapFont GenerateCardStatsFont(float size, float borderWidth)
    {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.hinting = FreeTypeFontGenerator.Hinting.Slight;
        param.spaceX = 0;
        param.kerning = true;
        param.borderColor = new Color(0F, 0F, 0F, 1F);
        param.borderWidth = borderWidth * Settings.scale;
        param.gamma = 0.9F;
        param.borderGamma = 0.9F;
        param.shadowColor = null; //new Color(0.0F, 0.0F, 0.0F, 0.5F);
        param.shadowOffsetX = 0;//Math.round(1.0F * Settings.scale);
        param.shadowOffsetY = 0;//Math.round(1.0F * Settings.scale);
        param.borderStraight = false;
        param.characters = "";
        param.incremental = true;
        param.size = Math.round(size * Settings.scale);
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(TitleFontSize.fontFile);
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
}

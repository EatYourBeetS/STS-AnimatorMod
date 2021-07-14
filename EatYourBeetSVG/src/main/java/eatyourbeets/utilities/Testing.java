package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Testing
{
    private static Float[] values;
    private static int colorIndex = 0;
    private static int fontIndex = 0;
    private static final ArrayList<Field> fonts = new ArrayList<>();
    private static final ArrayList<Field> colors = new ArrayList<>();

    public static Float[]  GetValues()
    {
        return values;
    }

    public static void SetValues(Float[] newValues)
    {
        JUtils.LogInfo(Testing.class, "Setting Values: " + JUtils.JoinStrings(", ", newValues));

        values = newValues;
    }

    public static boolean TrySetValue(int index, float value)
    {
        if (values != null && values.length > index)
        {
            values[index] = value;
            return true;
        }

        return false;
    }

    public static Color TryGetColor()
    {
        return TryGetColor(Color.WHITE);
    }

    public static Color TryGetColor(Color defaultColor)
    {
        if (values != null && values.length >= 3)
        {
            return new Color(values[0], values[1], values[2], values.length > 3 ? values[3] : 1);
        }

        return defaultColor;
    }

    public static float TryGetValue(int index, float defaultValue)
    {
        if (values != null && values.length > index)
        {
            return values[index];
        }

        return defaultValue;
    }

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
            JUtils.LogInfo(Testing.class, field.getName());

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
            JUtils.LogInfo(Testing.class, field.getName());

            return (Color) field.get(null);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}

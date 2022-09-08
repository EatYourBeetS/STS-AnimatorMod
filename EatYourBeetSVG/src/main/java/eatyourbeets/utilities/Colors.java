package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;

public class Colors
{
    private static final Color BLACK = Color.BLACK.cpy();
    private static final Color WHITE = Color.WHITE.cpy();
    private static final Color GREEN = Settings.GREEN_TEXT_COLOR.cpy();
    private static final Color CREAM = Settings.CREAM_COLOR.cpy();
    private static final Color PURPLE = Settings.PURPLE_COLOR.cpy();
    private static final Color DARK_GRAY = Color.DARK_GRAY.cpy();
    private static final Color BLUE = Settings.BLUE_TEXT_COLOR.cpy();
    private static final Color GOLD = Settings.GOLD_COLOR.cpy();
    private static final Color RED = Settings.RED_TEXT_COLOR.cpy();
    private static final Color VIOLET = Color.VIOLET.cpy();
    private static final Color ORANGE = new Color(0.85f, 0.45f, 0.25f, 1f);
    private static final Color LIGHT_GREEN = Colors.Lerp(GREEN, WHITE, 0.3f);
    private static final Color LIGHT_YELLOW = Settings.LIGHT_YELLOW_COLOR.cpy();
    private static final Color LIGHT_ORANGE = new Color(0.9f, 0.65f, 0.3f, 1f);

    public static Color Copy(Color color, float a)
    {
        return new Color(color.r, color.g, color.b, a);
    }

    public static Color Lerp(Color current, Color target, float amount)
    {
        current = current.cpy();
        current.r = Mathf.Lerp(current.r, target.r, amount);
        current.g = Mathf.Lerp(current.g, target.g, amount);
        current.b = Mathf.Lerp(current.b, target.b, amount);
        current.a = Mathf.Lerp(current.a, target.a, amount);
        return current;
    }

    public static Color Lerp(Color current, Color target, float amount, float alpha)
    {
        current = current.cpy();
        current.r = Mathf.Lerp(current.r, target.r, amount);
        current.g = Mathf.Lerp(current.g, target.g, amount);
        current.b = Mathf.Lerp(current.b, target.b, amount);
        current.a = alpha;
        return current;
    }

    public static Color Grayscale(float rgb, float a)
    {
        return new Color(rgb, rgb, rgb, a);
    }

    public static Color Random(float min, float max, boolean grayscale)
    {
        if (grayscale)
        {
            final float value = MathUtils.random(min, max);
            return new Color(value, value, value, 1);
        }

        return new Color(MathUtils.random(min, max), MathUtils.random(min, max), MathUtils.random(min, max), 1);
    }

    public static Color Black(float a)
    {
        BLACK.a = a;
        return BLACK;
    }

    public static Color White(float a)
    {
        WHITE.a = a;
        return WHITE;
    }

    public static Color DarkGray(float a)
    {
        DARK_GRAY.a = a;
        return DARK_GRAY;
    }

    public static Color Cream(float a)
    {
        CREAM.a = a;
        return CREAM;
    }

    public static Color Purple(float a)
    {
        PURPLE.a = a;
        return PURPLE;
    }

    public static Color Green(float a)
    {
        GREEN.a = a;
        return GREEN;
    }

    public static Color LightGreen(float a)
    {
        LIGHT_GREEN.a = a;
        return LIGHT_GREEN;
    }

    public static Color LightOrange(float a)
    {
        LIGHT_ORANGE.a = a;
        return LIGHT_ORANGE;
    }

    public static Color LightYellow(float a)
    {
        LIGHT_YELLOW.a = a;
        return LIGHT_YELLOW;
    }

    public static Color Blue(float a)
    {
        BLUE.a = a;
        return BLUE;
    }

    public static Color Gold(float a)
    {
        GOLD.a = a;
        return GOLD;
    }

    public static Color Red(float a)
    {
        RED.a = a;
        return RED;
    }

    public static Color Violet(float a)
    {
        VIOLET.a = a;
        return VIOLET;
    }

    public static Color Orange(float a)
    {
        ORANGE.a = a;
        return ORANGE;
    }
}

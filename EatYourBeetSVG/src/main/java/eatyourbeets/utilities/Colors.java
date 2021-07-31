package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;

public class Colors
{
    private static final Color BLACK = Color.BLACK.cpy();
    private static final Color WHITE = Color.WHITE.cpy();
    private static final Color CREAM = Settings.CREAM_COLOR.cpy();
    private static final Color GREEN = Settings.GREEN_TEXT_COLOR.cpy();
    private static final Color BLUE = Settings.BLUE_TEXT_COLOR.cpy();
    private static final Color GOLD = Settings.GOLD_COLOR.cpy();
    private static final Color RED = Settings.RED_TEXT_COLOR.cpy();

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

    public static Color Cream(float a)
    {
        CREAM.a = a;
        return CREAM;
    }

    public static Color Green(float a)
    {
        GREEN.a = a;
        return GREEN;
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
}

package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;

public class ColoredString
{
    public final Color color;
    public final String text;

    public ColoredString(Color color, Object text)
    {
        this.color = color.cpy();
        this.text = String.valueOf(text);
    }

    public ColoredString(String text, Color color)
    {
        this.color = color.cpy();
        this.text = text;
    }
}

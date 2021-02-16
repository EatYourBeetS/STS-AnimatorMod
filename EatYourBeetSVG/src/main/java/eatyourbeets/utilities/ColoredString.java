package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;

public class ColoredString
{
    public Color color;
    public String text;

    public ColoredString(Object text, Color color, float alpha)
    {
        this.text = String.valueOf(text);
        this.color = color;

        if (this.color != null)
        {
            this.color.a = alpha;
        }
    }

    public ColoredString(Object text, Color color)
    {
        this(text, color, 1);
    }

    public ColoredString(Object text)
    {
        this(text, Color.WHITE.cpy());
    }

    public ColoredString()
    {
        this("");
    }
}

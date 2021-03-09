package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;

public class ColoredString
{
    public Color color;
    public String text;

    public ColoredString(Object text, Color color, float alpha)
    {
        this.text = String.valueOf(text);

        if (color != null)
        {
            this.color = color.cpy();
            this.color.a = alpha;
        }
    }

    public ColoredString(Object text, Color color)
    {
        this(text, color, 1);
    }

    public ColoredString(Object text)
    {
        this(text, Color.WHITE);
    }

    public ColoredString()
    {
        this("");
    }
}

package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;

public class ColoredString
{
    public Color color;
    public String text;

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

    public ColoredString()
    {

    }
}

package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;

public class ColoredString
{
    public Color color;
    public String text;

    public ColoredString(Object text, Color color, float alpha)
    {
        this.color = color.cpy();
        this.color.a = alpha;
        this.text = String.valueOf(text);
    }

    public ColoredString(String text, Color color)
    {
        this.color = color.cpy();
        this.text = text;
    }

    public ColoredString(String text, Color color, float alpha)
    {
        this.color = color.cpy();
        this.color.a = alpha;
        this.text = text;
    }

    public ColoredString()
    {

    }
}

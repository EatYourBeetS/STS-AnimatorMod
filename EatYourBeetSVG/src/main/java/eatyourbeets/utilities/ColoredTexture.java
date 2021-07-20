package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class ColoredTexture
{
    public Color color;
    public Texture texture;
    public float scale;

    public ColoredTexture(Texture texture, Color color, float alpha)
    {
        this.scale = 1;
        this.texture = texture;

        if (color != null)
        {
            this.color = color.cpy();
            this.color.a = alpha;
        }
    }

    public ColoredTexture(Texture texture, Color color)
    {
        this(texture, color, 1);
    }

    public ColoredTexture(Texture texture)
    {
        this(texture, Color.WHITE);
    }

    public int GetWidth()
    {
        return texture.getWidth();
    }

    public int GetHeight()
    {
        return texture.getHeight();
    }

    public ColoredTexture SetColor(Color color)
    {
        this.color.r = color.r;
        this.color.g = color.g;
        this.color.b = color.b;
        this.color.a = color.a;

        return this;
    }
}

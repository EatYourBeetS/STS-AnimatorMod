package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;

public class TextureRenderer
{
    public boolean hide;
    public Hitbox hb;
    public Texture texture;
    public Color color;
    public float rotation;
    public float scaleX = 1;
    public float scaleY = 1;
    public int srcWidth;
    public int srcHeight;
    public boolean flipX;
    public boolean flipY;

    public TextureRenderer(Texture texture, Color color)
    {
        this.texture = texture;
        this.color = color;
        this.srcWidth = texture.getWidth();
        this.srcHeight = texture.getHeight();
    }

    public void Draw(SpriteBatch sb)
    {
        if (hide)
        {
            return;
        }

        Draw(sb, hb);
    }

    public void Draw(SpriteBatch sb, Hitbox hb)
    {
        if (hide)
        {
            return;
        }

        sb.setColor(color);
        sb.draw(texture, hb.x, hb.y, 0, 0, hb.width, hb.height, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
    }

    public void Draw(SpriteBatch sb, float x, float y, float width, float height)
    {
        if (hide)
        {
            return;
        }

        sb.setColor(color);
        sb.draw(texture, x, y, 0, 0, width, height, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
    }

    public TextureRenderer SetHidden(boolean hide)
    {
        this.hide = hide;

        return this;
    }

    public TextureRenderer SetHitbox(Hitbox hb)
    {
        this.hb = hb;

        return this;
    }

    public TextureRenderer SetColor(float r, float g, float b, float a)
    {
        this.color = new Color(r, g, b, a);

        return this;
    }

    public TextureRenderer SetColor(Color color)
    {
        this.color = color;

        return this;
    }

    public TextureRenderer SetFlipping(boolean flipX, boolean flipY)
    {
        this.flipX = flipX;
        this.flipY = flipY;

        return this;
    }

    public TextureRenderer SetOriginalDimensions(int srcWidth, int srcHeight)
    {
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;

        return this;
    }

    public TextureRenderer SetScale(float scaleX, float scaleY)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        return this;
    }

    public TextureRenderer SetRotation(float rotation)
    {
        this.rotation = rotation;

        return this;
    }
}

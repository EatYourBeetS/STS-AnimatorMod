package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.ui.GUIElement;

public class GUI_Image extends GUIElement
{
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

    public GUI_Image(Texture texture)
    {
        this(texture, Color.WHITE);
    }

    public GUI_Image(Texture texture, Hitbox hb)
    {
        this(texture, Color.WHITE);

        this.hb = hb;
    }

    public GUI_Image(Texture texture, Color color)
    {
        this.texture = texture;
        this.color = color.cpy();
        this.srcWidth = texture.getWidth();
        this.srcHeight = texture.getHeight();
    }

    @Override
    public void Update()
    {
        if (hb != null)
        {
            hb.update();
        }
    }

    public void Render(SpriteBatch sb)
    {
        Render(sb, hb);

        hb.render(sb);
    }

    public void Render(SpriteBatch sb, Hitbox hb)
    {
        Render(sb, hb.x, hb.y, hb.width, hb.height);
    }

    public void Render(SpriteBatch sb, float x, float y, float width, float height)
    {
        sb.setColor(color);
        sb.draw(texture, x, y, 0, 0, width, height, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
    }

    public void RenderCentered(SpriteBatch sb, float x, float y, float width, float height)
    {
        sb.setColor(color);
        sb.draw(texture, x, y, width/2f, height/2f, width, height, Settings.scale * scaleX,
                Settings.scale * scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
    }

    public GUI_Image Translate(float x, float y)
    {
        this.hb.translate(x, y);

        return this;
    }

    public GUI_Image Resize(float width, float height, float scale)
    {
        hb.resize(width * scale, height * scale);

        return this;
    }

    public GUI_Image SetTexture(Texture texture)
    {
        this.texture = texture;

        return this;
    }

    public GUI_Image SetHitbox(Hitbox hb)
    {
        this.hb = hb;

        return this;
    }

    public GUI_Image SetPosition(float x, float y)
    {
        this.hb.move(x, y);

        return this;
    }

    public GUI_Image SetColor(float r, float g, float b, float a)
    {
        this.color = new Color(r, g, b, a);

        return this;
    }

    public GUI_Image SetColor(Color color)
    {
        this.color = color.cpy();

        return this;
    }

    public GUI_Image SetFlipping(boolean flipX, boolean flipY)
    {
        this.flipX = flipX;
        this.flipY = flipY;

        return this;
    }

    public GUI_Image SetOriginalDimensions(int srcWidth, int srcHeight)
    {
        this.srcWidth = srcWidth;
        this.srcHeight = srcHeight;

        return this;
    }

    public GUI_Image SetScale(float scaleX, float scaleY)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        return this;
    }

    public GUI_Image SetRotation(float rotation)
    {
        this.rotation = rotation;

        return this;
    }
}

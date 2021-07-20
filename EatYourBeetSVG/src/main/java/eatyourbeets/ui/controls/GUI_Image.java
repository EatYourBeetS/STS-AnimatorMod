package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.ColoredTexture;

public class GUI_Image extends GUIElement
{
    public Hitbox hb;
    public ColoredTexture background;
    public ColoredTexture foreground;
    public Texture texture;
    public Color color;
    public float scaleX = 1;
    public float scaleY = 1;
    public float rotation;
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

    public GUI_Image SetBackgroundTexture(Texture texture, Color color, float scale)
    {
        this.background = new ColoredTexture(texture, color);
        this.background.scale = scale;

        return this;
    }

    public GUI_Image SetBackgroundTexture(Texture texture)
    {
        SetBackgroundTexture(texture, null, 1);

        return this;
    }

    public GUI_Image SetForegroundTexture(Texture texture, Color color, float scale)
    {
        this.foreground = new ColoredTexture(texture, color);
        this.foreground.scale = scale;

        return this;
    }

    public GUI_Image SetForegroundTexture(Texture texture)
    {
        SetForegroundTexture(texture, null, 1);

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

    public GUI_Image SetPosition(float cX, float cY)
    {
        this.hb.move(cX, cY);

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
        if (background != null)
        {
            final float w = width * background.scale;
            final float h = height * background.scale;
            sb.setColor(background.color != null ? background.color : color);
            sb.draw(background.texture, x + ((width-w)*0.5f), y + ((height-h)*0.5f), 0, 0, w, h, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
        }

        sb.setColor(color);
        sb.draw(texture, x, y, 0, 0, width, height, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);

        if (foreground != null)
        {
            final float w = width * foreground.scale;
            final float h = height * foreground.scale;
            sb.setColor(foreground.color != null ? foreground.color : color);
            sb.draw(foreground.texture, x + ((width-w)*0.5f), y + ((height-h)*0.5f), 0, 0, w, h, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
        }
    }

    public void RenderCentered(SpriteBatch sb, float x, float y, float width, float height)
    {
        if (background != null)
        {
            final float scale = background.scale;
            sb.setColor(background.color != null ? background.color : color);
            sb.draw(background.texture, x, y, width/2f, height/2f, width, height, scaleX * scale, scaleY * scale, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
        }

        sb.setColor(color);
        sb.draw(texture, x, y, width/2f, height/2f, width, height, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);

        if (foreground != null)
        {
            final float scale = foreground.scale;
            sb.setColor(foreground.color != null ? foreground.color : color);
            sb.draw(foreground.texture, x, y, width/2f, height/2f, width, height, scaleX * scale, scaleY * scale, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
        }
    }
}
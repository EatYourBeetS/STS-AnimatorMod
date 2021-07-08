package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.ui.GUIElement;

public class GUI_ImageRegion extends GUIElement
{
    public Hitbox hb;
    public TextureRegion texture;
    public TextureRegion background;
    public TextureRegion foreground;
    public Color color;
    public float rotation;
    public float scaleX = 1;
    public float scaleY = 1;

    public GUI_ImageRegion(TextureRegion texture)
    {
        this(texture, Color.WHITE);
    }

    public GUI_ImageRegion(TextureRegion texture, Hitbox hb)
    {
        this(texture, Color.WHITE);

        this.hb = hb;
    }

    public GUI_ImageRegion(TextureRegion texture, Color color)
    {
        this.texture = texture;
        this.color = color.cpy();
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
        if (background != null)
        {
            sb.draw(background, x, y, 0, 0, width, height, scaleX, scaleY, rotation);
        }
        sb.draw(texture, x, y, 0, 0, width, height, scaleX, scaleY, rotation);
        if (foreground != null)
        {
            sb.draw(foreground, x, y, 0, 0, width, height, scaleX, scaleY, rotation);
        }
    }

    public void RenderCentered(SpriteBatch sb, float x, float y, float width, float height)
    {
        sb.setColor(color);
        if (background != null)
        {
            sb.draw(background, x, y, width/2f, height/2f, width, height, Settings.scale * scaleX,Settings.scale * scaleY, rotation);
        }
        sb.draw(texture, x, y, width/2f, height/2f, width, height, Settings.scale * scaleX, Settings.scale * scaleY, rotation);
        if (foreground != null)
        {
            sb.draw(foreground, x, y, width/2f, height/2f, width, height, Settings.scale * scaleX, Settings.scale * scaleY, rotation);
        }
    }

    public GUI_ImageRegion Translate(float x, float y)
    {
        this.hb.translate(x, y);

        return this;
    }

    public GUI_ImageRegion Resize(float width, float height, float scale)
    {
        hb.resize(width * scale, height * scale);

        return this;
    }

    public GUI_ImageRegion SetBackgroundTexture(TextureRegion texture)
    {
        this.background = texture;

        return this;
    }

    public GUI_ImageRegion SetForegroundTexture(TextureRegion texture)
    {
        this.foreground = texture;

        return this;
    }


    public GUI_ImageRegion SetTexture(TextureRegion texture)
    {
        this.texture = texture;

        return this;
    }

    public GUI_ImageRegion SetHitbox(Hitbox hb)
    {
        this.hb = hb;

        return this;
    }

    public GUI_ImageRegion SetPosition(float x, float y)
    {
        this.hb.move(x, y);

        return this;
    }

    public GUI_ImageRegion SetColor(float r, float g, float b, float a)
    {
        this.color = new Color(r, g, b, a);

        return this;
    }

    public GUI_ImageRegion SetColor(Color color)
    {
        this.color = color.cpy();

        return this;
    }

    public GUI_ImageRegion SetScale(float scaleX, float scaleY)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        return this;
    }

    public GUI_ImageRegion SetRotation(float rotation)
    {
        this.rotation = rotation;

        return this;
    }
}
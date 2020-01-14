package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public abstract class GUIElement
{
    public static final Color HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.3F);
    public static final Color TEXT_DISABLED_COLOR = new Color(0.6F, 0.6F, 0.6F, 1.0F);

    public boolean active = true;

    public abstract void Update();
    public abstract void Render(SpriteBatch sb);

    public void TryRender(SpriteBatch sb)
    {
        if (active)
        {
            Render(sb);
        }
    }

    public void TryUpdate()
    {
        if (active)
        {
            Update();
        }
    }

    public GUIElement SetActive(boolean active)
    {
        this.active = active;

        return this;
    }

    protected static float Scale(float value)
    {
        return Settings.scale * value;
    }

    protected static float GetWidth(float value)
    {
        return Settings.WIDTH * value;
    }

    protected static float GetHeight(float value)
    {
        return Settings.HEIGHT * value;
    }
}

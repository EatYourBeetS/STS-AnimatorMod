package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.GameUtilities;

public abstract class GUIElement
{
    public static final Color HOVER_BLEND_COLOR = new Color(1f, 1f, 1f, 0.3f);
    public static final Color TEXT_DISABLED_COLOR = new Color(0.6f, 0.6f, 0.6f, 1f);

    public boolean isActive = true;

    public abstract void Update();
    public abstract void Render(SpriteBatch sb);

    public boolean TryRender(SpriteBatch sb)
    {
        if (isActive)
        {
            Render(sb);
        }

        return isActive;
    }

    public boolean TryUpdate()
    {
        if (isActive)
        {
            Update();
        }

        return isActive;
    }

    public GUIElement SetActive(boolean active)
    {
        this.isActive = active;

        return this;
    }

    public static AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GameUtilities.GetPlayerClass();
    }

    protected static float Scale(float value)
    {
        return Settings.scale * value;
    }

    protected static float ScreenW(float value)
    {
        return Settings.WIDTH * value;
    }

    protected static float ScreenH(float value)
    {
        return Settings.HEIGHT * value;
    }
}

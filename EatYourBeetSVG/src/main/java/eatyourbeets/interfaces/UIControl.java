package eatyourbeets.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public interface UIControl
{
    Color HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.3F);
    Color TEXT_DISABLED_COLOR = new Color(0.6F, 0.6F, 0.6F, 1.0F);

    void Update();
    void Render(SpriteBatch sb);

    static float Scale(float value)
    {
        return Settings.scale * value;
    }
}

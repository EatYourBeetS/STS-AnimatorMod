package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public class CircularWaveEffect extends EYBEffect
{
    protected float x;
    protected float y;
    protected float scaleLower = 0.2f;
    protected float scaleUpper = 3.10f;
    protected float vfxTimer;
    protected float vfxFrequency = 0.2f;
    protected Color endColor;

    public CircularWaveEffect(float startX, float startY)
    {
        super(1.0f);

        this.x = startX;
        this.y = startY;
        this.color = Color.WHITE;
        this.endColor = Color.NAVY;
    }

    public CircularWaveEffect SetColors(Color startColor, Color endColor) {
        this.color = startColor;
        this.endColor = endColor;
        return this;
    }

    public CircularWaveEffect SetScale(float scaleLower, float scaleUpper)
    {
        this.scaleLower = scaleLower;
        this.scaleUpper = scaleUpper;

        return this;
    }

    public CircularWaveEffect SetFrequency(float frequency)
    {
        this.vfxFrequency = Mathf.Clamp(frequency, 0.01f, startingDuration / 5f);

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        vfxTimer -= deltaTime;

        if (vfxTimer < 0f)
        {
            PCLGameEffects.Queue.Add(new FadingParticleEffect(VFX.IMAGES.Circle2.Texture(), x, y)
                            .SetBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                            .SetColor(color.cpy(), endColor.cpy(), 2f)
                            .SetScale(this.scaleLower)
                            .SetTargetScale(scaleUpper, scaleUpper - scaleLower + 0.5f))
                    .SetDuration(startingDuration * 1.5f,true);
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }
}

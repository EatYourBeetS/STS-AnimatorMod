package pinacolada.effects.vfx;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.PCLEffect;
import pinacolada.utilities.PCLGameEffects;

public class TornadoEffect extends PCLEffect
{
    protected float x;
    protected float y;
    protected float spreadX = 10f * Settings.scale;
    protected float spreadY = 10f * Settings.scale;
    protected float scaleLower = 0.2f;
    protected float scaleUpper = 1f;
    protected float vfxTimer;
    protected float vfxFrequency = 0.01f;

    public TornadoEffect(float startX, float startY)
    {
        super(0.5f);

        this.x = startX;
        this.y = startY;
    }

    public TornadoEffect SetScale(float scaleLower, float scaleUpper)
    {
        this.scaleLower = scaleLower;
        this.scaleUpper = scaleUpper;

        return this;
    }

    public TornadoEffect SetSpread(float spreadX, float spreadY)
    {
        this.spreadX = spreadX;
        this.spreadY = spreadY;

        return this;
    }

    public TornadoEffect SetFrequency(float frequency)
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
            final float x = this.x + Random(-spreadX, spreadX);
            final float y = this.y + Random(-spreadY, spreadY);
            PCLGameEffects.Queue.Add(new TornadoParticleEffect(x, y,
                    Random(15f, 39f)  * Settings.scale));
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }
}

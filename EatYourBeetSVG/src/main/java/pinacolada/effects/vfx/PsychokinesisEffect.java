package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.PCLEffect;
import pinacolada.utilities.PCLGameEffects;

public class PsychokinesisEffect extends PCLEffect
{
    protected float x;
    protected float y;
    protected float spreadX = 10f * Settings.scale;
    protected float spreadY = 10f * Settings.scale;
    protected float spreadGrowth = 2.2f * Settings.scale;
    protected float scaleLower = 0.2f;
    protected float scaleUpper = 1.35f;
    protected float scaleGrowth = -0.020f;
    protected float vfxTimer;
    protected float vfxFrequency = 0.04f;
    protected float vfxFrequencyGrowth = -0.006f;

    public PsychokinesisEffect(float startX, float startY)
    {
        super(0.5f);

        this.x = startX;
        this.y = startY;
    }

    public PsychokinesisEffect SetScale(float scaleLower, float scaleUpper)
    {
        this.scaleLower = scaleLower;
        this.scaleUpper = scaleUpper;

        return this;
    }

    public PsychokinesisEffect SetSpread(float spreadX, float spreadY)
    {
        this.spreadX = spreadX;
        this.spreadY = spreadY;

        return this;
    }

    public PsychokinesisEffect SetFrequency(float frequency)
    {
        this.vfxFrequency = Mathf.Clamp(frequency, 0.01f, startingDuration / 5f);

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        vfxTimer -= deltaTime;
        spreadX += spreadGrowth;
        spreadY += spreadGrowth;
        scaleLower += scaleGrowth;
        scaleUpper += scaleGrowth;

        if (vfxTimer < 0f)
        {
            final float x = this.x + Random(-spreadX, spreadX);
            final float y = this.y + Random(-spreadY, spreadY);
            final float scale = Random(Math.max(0.05f,this.scaleLower),this.scaleUpper);
            final Color color = new Color(MathUtils.random(0.8f, 1f), MathUtils.random(0.7f, 1f), 1, 1);
            if (RandomBoolean(0.2f)) {
                PCLGameEffects.Queue.Add(new FadingParticleEffect(PCLEffect.IMAGES.Circle.Texture(), x, y)
                        .SetColor(Colors.Random(0.83f, 1f, false))
                        .SetScale(this.scaleLower * 0.05f)
                        .SetTargetScale(scale * 2, 5f))
                        .SetDuration(1.5f,true);
            }
            else {
                PCLGameEffects.Queue.Add(new GenericAnimationEffect(PCLEffect.IMAGES.Psi.Texture(), x, y, 5, 5, 0.01f)
                        .SetColor(Colors.Random(0.83f, 1f, false))
                        .SetScale(this.scaleLower * 0.05f)
                        .SetTargetScale(scale, 5f));
            }

            vfxFrequency += vfxFrequencyGrowth;
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }
}

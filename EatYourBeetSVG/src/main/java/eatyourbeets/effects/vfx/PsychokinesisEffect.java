package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.Mathf;

public class PsychokinesisEffect extends EYBEffect
{
    protected float x;
    protected float y;
    protected float spreadX = 10f * Settings.scale;
    protected float spreadY = 10f * Settings.scale;
    protected float spreadGrowth = 2.4f * Settings.scale;
    protected float scaleLower = 0.2f;
    protected float scaleUpper = 1f;
    protected float scaleGrowth = -0.02f;
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
            final float scale = Random(Math.max(0,this.scaleLower),this.scaleUpper);
            GameEffects.Queue.Add(new GenericAnimationEffect(EYBEffect.IMAGES.Psi.Texture(), x, y, 5, 5, 0.01f).SetColor(Color.WHITE)).SetScale(this.scaleLower * 0.05f).SetTargetScale(scale, 5f);
            vfxFrequency += vfxFrequencyGrowth;
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }
}

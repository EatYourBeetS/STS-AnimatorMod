package eatyourbeets.effects.vfx;

import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.Mathf;

public class ShootingStarsEffect extends EYBEffect
{
    protected float x;
    protected float y;
    protected float spreadX;
    protected float spreadY;
    protected float vfxTimer;
    protected float vfxFrequency = 0.021f;
    protected float horizontalSpeedMin = 2150;
    protected float horizontalSpeedMax = 2650;
    protected float verticalSpeedMin;
    protected float verticalSpeedMax;
    protected boolean flipHorizontally;

    public ShootingStarsEffect(float startX, float startY)
    {
        super(0.5f);

        this.x = startX;
        this.y = startY;
    }

    public ShootingStarsEffect FlipHorizontally(boolean flip)
    {
        this.flipHorizontally = flip;

        return this;
    }

    public ShootingStarsEffect SetSpread(float spreadX, float spreadY)
    {
        this.spreadX = spreadX;
        this.spreadY = spreadY;

        return this;
    }

    public ShootingStarsEffect SetHorizontalSpeed(float min, float max)
    {
        this.horizontalSpeedMin = min;
        this.horizontalSpeedMax = max;

        return this;
    }

    public ShootingStarsEffect SetVerticalSpeed(float min, float max)
    {
        this.horizontalSpeedMin = min;
        this.horizontalSpeedMax = max;

        return this;
    }

    public ShootingStarsEffect SetFrequency(float frequency)
    {
        this.vfxFrequency = Mathf.Clamp(frequency, 0.01f, startingDuration / 5f);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (flipHorizontally)
        {
            horizontalSpeedMin = -horizontalSpeedMin;
            horizontalSpeedMax = -horizontalSpeedMax;
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            final float x = this.x + Random(-spreadX, spreadX);
            final float y = this.y + Random(-spreadY, spreadY);
            final float h_speed = Random(horizontalSpeedMin, horizontalSpeedMax);
            final float v_speed = Random(verticalSpeedMin, verticalSpeedMax);
            GameEffects.Queue.Add(new StarEffect(x, y, h_speed, v_speed));
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }
}

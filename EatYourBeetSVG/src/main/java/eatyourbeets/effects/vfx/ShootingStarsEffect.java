package eatyourbeets.effects.vfx;

import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class ShootingStarsEffect extends EYBEffect
{
    protected float x;
    protected float y;
    protected float spreadY;
    protected float vfxTimer;
    protected float horizontalSpeedMin;
    protected float horizontalSpeedMax;

    public ShootingStarsEffect(float startX, float startY, float spreadY, boolean flipHorizontally)
    {
        super(0.5f);

        this.x = startX;
        this.y = startY;
        this.spreadY = spreadY;

        if (flipHorizontally)
        {
            this.horizontalSpeedMin = -1800;
            this.horizontalSpeedMax = -2300;
        }
        else
        {
            this.horizontalSpeedMin = 1800;
            this.horizontalSpeedMax = 2300;
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            vfxTimer = 0.025f;
            GameEffects.Queue.Add(new StarEffect(x, y + Random(-spreadY, spreadY), horizontalSpeedMin, horizontalSpeedMax, 0f, 0));
        }

        super.UpdateInternal(deltaTime);
    }
}

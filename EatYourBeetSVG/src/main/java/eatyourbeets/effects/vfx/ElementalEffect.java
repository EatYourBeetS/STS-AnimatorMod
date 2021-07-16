package eatyourbeets.effects.vfx;

import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class ElementalEffect extends EYBEffect
{
    protected float x;
    protected float y;
    protected float spreadY;
    protected float vfxTimer;

    public ElementalEffect(float startX, float startY, float spreadY)
    {
        super(0.5f);

        this.x = startX;
        this.y = startY;
        this.spreadY = spreadY;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {

        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            vfxTimer = 0.025f;
            GameEffects.Queue.Add(new StarEffect(x, y + Random(-spreadY, spreadY), 1800f, 2300f));
        }

        if (TickDuration(deltaTime)) Complete();
    }
}

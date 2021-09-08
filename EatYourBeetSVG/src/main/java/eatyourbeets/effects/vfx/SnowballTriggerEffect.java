package eatyourbeets.effects.vfx;

import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class SnowballTriggerEffect extends EYBEffect
{
    protected float x;
    protected float y;
    protected float count;

    public SnowballTriggerEffect(float x, float y)
    {
        this(x,y,25);
    }

    public SnowballTriggerEffect(float x, float y, float count)
    {
        this.x = x;
        this.y = y;
        this.count = count;
    }

    @Override
    protected void FirstUpdate()
    {
        for (int i = 0; i < count; ++i)
        {
            GameEffects.Queue.Add(new SnowballParticleEffect(this.x, this.y, color).SetRealtime(isRealtime));
            GameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, color));
        }

        Complete();
    }
}

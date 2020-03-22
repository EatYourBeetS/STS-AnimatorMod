package eatyourbeets.effects.vfx;

import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class GhostIgniteEffect extends EYBEffect
{
    protected float x;
    protected float y;

    public GhostIgniteEffect(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void FirstUpdate()
    {
        for (int i = 0; i < 25; ++i)
        {
            GameEffects.Queue.Add(new FireBurstParticleEffect(this.x, this.y, color).SetRealtime(isRealtime));
            GameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, color));
        }

        Complete();
    }
}

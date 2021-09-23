package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class SnowballImpactEffect extends EYBEffect
{
    protected int particles = 25;
    protected float x;
    protected float y;

    public SnowballImpactEffect(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.color = Color.SKY.cpy();
    }

    public SnowballImpactEffect SetParticleCount(int particles)
    {
        this.particles = particles;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        for (int i = 0; i < particles; i++)
        {
            GameEffects.Queue.Add(new SnowballParticleEffect(this.x, this.y, color).SetRealtime(isRealtime));
            GameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, color));
        }

        Complete();
    }
}

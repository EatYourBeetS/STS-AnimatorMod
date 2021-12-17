package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import pinacolada.effects.PCLEffect;
import pinacolada.utilities.PCLGameEffects;

public class SnowballImpactEffect extends PCLEffect
{
    protected int particles = 40;
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
            PCLGameEffects.Queue.Add(new SnowballParticleEffect(this.x, this.y, color).SetDuration(0.75f, isRealtime));
            PCLGameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, color));
        }

        Complete();
    }
}

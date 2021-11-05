package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class SparkImpactEffect extends EYBEffect
{
    private static final TextureCache[] images = {IMAGES.Spark1, IMAGES.Spark2};
    protected int particles = 75;
    protected float radius = 300;
    protected float x;
    protected float y;

    public SparkImpactEffect(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.color = Color.WHITE.cpy();
    }

    public SparkImpactEffect SetParticleCount(int particles)
    {
        this.particles = particles;

        return this;
    }

    public SparkImpactEffect SetRadius(float radius)
    {
        this.radius = radius;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        for (int i = 0; i < particles; i++)
        {
            GameEffects.Queue.Add(new FadingParticleEffect(JUtils.Random(images).Texture(), x, y)
                    .SetColor(Colors.Random(0.70f,1f,false))
                    .SetTranslucent(1f)
                    .Edit(i, (r, p) -> p
                            .SetScale(scale * MathUtils.random(0.1f, 0.75f)).SetTargetRotation(36000f,1440f)
                            .SetSpeed(MathUtils.random(150f, 200f), MathUtils.random(150f, 200f), MathUtils.random(700f, 1010f),0f)
                            .SetAcceleration(0f, MathUtils.random(5f, 25f) * -1, null, null, null)
                            .SetTargetPosition(x + radius * MathUtils.cos(r), y + radius * MathUtils.sin(r))).SetDuration(0.5f, false));
            //GameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, color));
        }

        Complete();
    }
}

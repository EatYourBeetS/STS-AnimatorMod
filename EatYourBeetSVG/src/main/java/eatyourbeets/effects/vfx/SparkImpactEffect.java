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
    protected float radius = 500;
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
                    .SetColor(Colors.Random(0.5f,1f,false))
                    .SetTranslucent(1f)
                    .Edit(i, (r, p) -> p
                            .SetScale(scale * MathUtils.random(0.04f, 0.55f)).SetTargetRotation(36000f,1440f)
                            .SetSpeed(MathUtils.random(170f, 800f), MathUtils.random(170f, 800f), MathUtils.random(700f, 1010f),0f)
                            .SetAcceleration(MathUtils.random(-15f, 15f), MathUtils.random(25f, 125f) * -1, null, null, null)
                            .SetTargetPosition(x + radius * MathUtils.cos(r), y + radius * MathUtils.sin(r))).SetDuration(0.5f, false)
            .SetDuration(MathUtils.random(0.5F, 1.0F), true));
            //GameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, color));
        }

        Complete();
    }
}

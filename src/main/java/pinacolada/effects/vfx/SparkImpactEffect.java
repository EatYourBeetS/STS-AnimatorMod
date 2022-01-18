package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.ui.TextureCache;
import eatyourbeets.utilities.Colors;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLJUtils;
import pinacolada.utilities.PCLRenderHelpers;

public class SparkImpactEffect extends EYBEffect
{
    private static final TextureCache[] images = {VFX.IMAGES.Spark1, VFX.IMAGES.Spark2};
    protected int particles = 60;
    protected float radius = 500;
    protected float x;
    protected float y;

    public SparkImpactEffect(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.color = Color.GOLDENROD.cpy();
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
            PCLGameEffects.Queue.Add(new FadingParticleEffect(PCLJUtils.Random(images).Texture(), x, y)
                    .SetColor(Colors.Random(0.5f,1f,false))
                    .SetBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                    .Edit(i, (r, p) -> p
                            .SetScale(scale * MathUtils.random(0.04f, 0.55f)).SetTargetRotation(36000f,1440f)
                            .SetSpeed(MathUtils.random(170f, 800f), MathUtils.random(170f, 800f), MathUtils.random(700f, 1010f),-0.03f)
                            .SetTargetPosition(x + radius * MathUtils.cos(r), y + radius * MathUtils.sin(r))).SetDuration(0.5f, false)
            .SetDuration(MathUtils.random(0.5F, 1.0F), true));
            PCLGameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, color));
        }

        Complete();
    }
}

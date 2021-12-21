package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.VFX;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public class RazorWindEffect extends EYBEffect
{
    public static final TextureCache image = VFX.IMAGES.AirSlice;
    protected static final Color PARTICLE_COLOR = Color.LIME.cpy();

    protected float x;
    protected float y;
    protected float targetY;
    protected float horizontalAcceleration;
    protected float horizontalSpeed;
    protected float rotationSpeed;
    protected float vfxTimer;
    protected float vfxFrequency;

    public RazorWindEffect(float x, float y, float targetY, float horizontalSpeed, float horizontalAcceleration)
    {
        super(1f);

        this.x = x;
        this.y = y;
        this.targetY = targetY;
        this.scale = 1;
        this.horizontalSpeed = horizontalSpeed * Settings.scale;
        this.horizontalAcceleration = horizontalSpeed * Settings.scale;
        this.rotation = Random(5f, 10f);
        this.rotationSpeed = Random(1000f, 1200f);
        this.color = Color.WHITE.cpy();
        this.vfxFrequency = 0.01f;
    }

    public RazorWindEffect SetFrequency(float frequency)
    {
        this.vfxFrequency = Mathf.Clamp(frequency, 0.01f, startingDuration / 5f);

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x += horizontalSpeed * deltaTime;
        y = Interpolation.pow2OutInverse.apply(y, targetY, Math.min(1f, (1f - duration) / 2f));
        horizontalSpeed += horizontalAcceleration * deltaTime;
        rotation += rotationSpeed * deltaTime;

        if ((1f - duration) < 0.1f)
        {
            color.a = Interpolation.fade.apply(0.1f, 1f, (1f - duration) * 7f);
        }
        else
        {
            color.a = Interpolation.pow2Out.apply(0.1f, 1f, duration);
        }

        vfxTimer -= deltaTime;
        if (vfxTimer < 0f)
        {
            PCLGameEffects.Queue.Add(new RazorWindParticleEffect(x, y + (Random(-100, 100) * Settings.scale),
                    Random(-300f, -50f) * Math.signum(horizontalSpeed), Random(-200f, 200f)));
            PCLGameEffects.Queue.Add(new FadingParticleEffect(image.Texture(), x, y)
                            .SetBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                            .SetOpacity(0.5f)
                            .Edit(rotation, (r, p) -> p
                                    .SetColor(new Color(MathUtils.random(0.8f, 1f), 1f, MathUtils.random(0.8f, 1f), 0.5f))
                                    .SetRotation(rotation)
                                    .SetScale(scale).SetTargetRotation(36000, null)
                                    .SetSpeed(0f, 0f, Random(300f, 400f), null)));
            PCLGameEffects.Queue.Add(new LightFlareParticleEffect(this.x, this.y, PARTICLE_COLOR));
            vfxTimer = vfxFrequency;
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        RenderImage(sb, image.Texture(), x, y, false, false);
    }
}

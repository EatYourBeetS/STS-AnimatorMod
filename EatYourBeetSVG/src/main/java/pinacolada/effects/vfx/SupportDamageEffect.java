package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.PCLEffect;
import pinacolada.effects.Projectile;
import pinacolada.effects.VFX;
import pinacolada.ui.TextureCache;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLRenderHelpers;

public class SupportDamageEffect extends PCLEffect
{
    private static final TextureCache image = IMAGES.Slice;
    protected Hitbox sourceHb;
    protected Hitbox targetHb;
    protected int totalParticles = 5;
    protected int currentParticles = 0;
    protected float vY;
    protected float vfxFrequency = 0.015f;
    protected float vfxTimer = vfxFrequency;

    public SupportDamageEffect(Hitbox sourceHb, Hitbox targetHb)
    {
        this.sourceHb = sourceHb;
        this.targetHb = targetHb;
        this.color = Color.WHITE.cpy();
    }

    public SupportDamageEffect SetParticleCount(int particles)
    {
        this.totalParticles = particles;
        this.currentParticles = 0;

        return this;
    }

    public SupportDamageEffect SetSpeed(float vY)
    {
        this.vY = vY;

        return this;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        vfxTimer -= deltaTime;

        if (vfxTimer < 0f)
        {
            float colorValue = 1f - currentParticles / (float) totalParticles;
            final Projectile projectile = new Projectile(image.Texture(), 256f, 256f)
                    .SetBlendingMode(PCLRenderHelpers.BlendingMode.Glowing)
                    .SetColor(new Color(colorValue, colorValue, colorValue, colorValue))
                    .SetRotation(Mathf.GetAngle(sourceHb.cX, sourceHb.cY, targetHb.cX, targetHb.cY))
                    .SetPosition(sourceHb.cX, sourceHb.cY);
            PCLGameEffects.Queue.Add(new ThrowProjectileEffect(projectile, targetHb)
                    .AddCallback(hb -> PCLGameEffects.Queue.Add(VFX.Gunshot(targetHb, 0f).SetBlendingMode(pinacolada.utilities.PCLRenderHelpers.BlendingMode.Glowing).SetColor(Colors.Lerp(Color.SCARLET, Color.GOLD, MathUtils.random(0f,1f))))
                            .SetDuration(duration, true)));
            vfxTimer = vfxFrequency;
            currentParticles += 1;
            if (currentParticles >= totalParticles) {
                Complete();
            }
        }

        super.UpdateInternal(deltaTime);
    }
}

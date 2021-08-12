package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.combat.*;
import eatyourbeets.effects.utility.CombinedEffect;
import eatyourbeets.effects.vfx.*;
import eatyourbeets.effects.vfx.megacritCopy.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

public class VFX
{
    public static float RandomX(Hitbox hb, float variance)
    {
        return hb.cX + (variance == 0 ? 0 : (MathUtils.random(-variance, variance) * hb.width));
    }

    public static float RandomY(Hitbox hb, float variance)
    {
        return hb.cY + (variance == 0 ? 0 : (MathUtils.random(-variance, variance) * hb.height));
    }

    public static CataclysmEffect Cataclysm()
    {
        return new CataclysmEffect();
    }

    public static MeteorFallEffect MeteorFall(Hitbox hb)
    {
        return new MeteorFallEffect(RandomX(hb, 0.2f), RandomY(hb, 0.2f));
    }

    public static boolean FlipHorizontally()
    {
        return AbstractDungeon.player.flipHorizontal || AbstractDungeon.getMonsters().shouldFlipVfx();
    }

    public static BiteEffect2 Bite(Hitbox target)
    {
        return Bite(target, Color.WHITE);
    }

    public static BiteEffect2 Bite(Hitbox target, Color color)
    {
        return new BiteEffect2(target.cX, target.cY - (40.0F * Settings.scale), color);
    }

    public static ClashEffect2 Clash(Hitbox target)
    {
        return new ClashEffect2(target.cX, target.cY);
    }

    public static CleaveEffect Cleave(boolean fromPlayer)
    {
        return new CleaveEffect(fromPlayer);
    }

    public static ClawEffect2 Claw(Hitbox target, Color color1, Color color2)
    {
        return Claw(RandomX(target, 0.2f), RandomY(target, 0.2f), color1, color2);
    }

    public static ClawEffect2 Claw(float cX, float cY, Color color1, Color color2)
    {
        return new ClawEffect2(cX, cY, color1, color2);
    }

    public static CombinedEffect Dark(Hitbox hb, int variance)
    {
        return Dark(RandomX(hb, variance), RandomY(hb, variance));
    }

    public static CombinedEffect Dark(float cX, float cY)
    {
        final CombinedEffect effect = new CombinedEffect();
        effect.Add(new OrbFlareEffect2(cX, cY).SetColors(OrbFlareEffect.OrbFlareColor.DARK)).renderBehind = false;
        for (int i = 0; i < 4; i++)
        {
            effect.Add(new DarkOrbActivateParticle(cX, cY)).renderBehind = false;
        }

        return effect;
    }

    public static MindblastEffect2 Mindblast(float dialogX, float dialogY)
    {
        return new MindblastEffect2(dialogX, dialogY, FlipHorizontally());
    }

    public static ShockWaveEffect ShockWave(Hitbox source, Color color)
    {
        return ShockWave(source, color, ShockWaveEffect.ShockWaveType.ADDITIVE);
    }

    public static ShockWaveEffect ShockWave(Hitbox source, Color color, ShockWaveEffect.ShockWaveType type)
    {
        return new ShockWaveEffect(source.cX, source.cY, color.cpy(), type);
    }

    public static ColoredSweepingBeamEffect SweepingBeam(AbstractCreature source)
    {
        return SweepingBeam(source.hb, source.flipHorizontal, Color.CYAN);
    }

    public static ColoredSweepingBeamEffect SweepingBeam(Hitbox source, boolean flipHorizontal, Color color)
    {
        return new ColoredSweepingBeamEffect(source.cX, source.cY, flipHorizontal, color);
    }

    public static DaggerSprayEffect DaggerSpray()
    {
        return new DaggerSprayEffect(FlipHorizontally());
    }

    public static GenericAnimationEffect Darkness(Hitbox target, float spread)
    {
        return Darkness(RandomX(target, spread), RandomY(target, spread));
    }

    public static GenericAnimationEffect Darkness(float cX, float cY)
    {
        return new GenericAnimationEffect(EYBEffect.IMAGES.Darkness.Texture(), cX, cY, 4, 5, 0.01f)
                .SetColor(Color.WHITE)
                .SetRotation(0f, 1800f)
                .SetScale(0f)
                .SetTargetScale(1f,5f)
                .SetMode(AnimatedProjectile.AnimationMode.Loop, 240)
                .SetFading(30);
    }

    public static ExplosionSmallEffect SmallExplosion(Hitbox source)
    {
        return new ExplosionSmallEffect(source.cX, source.cY);
    }

    public static ExplosionSmallEffect SmallExplosion(Hitbox source, float variance)
    {
        return new ExplosionSmallEffect(RandomX(source, variance), RandomY(source, variance));
    }

    public static LaserBeamEffect2 Laser(Hitbox source, Color color)
    {
        return new LaserBeamEffect2(source.cX, source.cY).SetColor(color);
    }

    public static SmallLaserEffect2 SmallLaser(Hitbox source, Hitbox target, Color color)
    {
        return new SmallLaserEffect2(source.cX, source.cY, RandomX(target, 0.2f), RandomY(target, 0.2f))
        .SetColors(color, Colors.Lerp(color, Color.BLACK, 0.3f));
    }

    public static FallingIceEffect FallingIce(int frostCount)
    {
        return new FallingIceEffect(frostCount, FlipHorizontally());
    }

    public static FireballEffect2 Fireball(Hitbox source, Hitbox target)
    {
        return new FireballEffect2(source.cX, source.cY, target.cX, target.cY);
    }

    public static GenericAnimationEffect FireBurst(float cX, float cY)
    {
        return new GenericAnimationEffect(EYBEffect.IMAGES.FireBurst.Texture(), cX, cY, 8, 8);
    }

    public static FlameBarrierEffect FlameBarrier(Hitbox source)
    {
        return new FlameBarrierEffect(source.cX, source.cY);
    }

    public static GenericAnimationEffect Gunshot(Hitbox target, float spread)
    {
        return Gunshot(RandomX(target, spread), RandomY(target, spread));
    }

    public static GenericAnimationEffect Gunshot(float cX, float cY)
    {
        return new GenericAnimationEffect(EYBEffect.IMAGES.Shot.Texture(), cX, cY, 4, 4).SetColor(Color.DARK_GRAY);
    }

    public static HemokinesisEffect2 Hemokinesis(Hitbox source, Hitbox target)
    {
        return new HemokinesisEffect2(target.cX, target.cY, source.cX, source.cY);
    }

    public static LightningEffect2 Lightning(Hitbox target)
    {
        return Lightning(target.cX, target.cY);
    }

    public static LightningEffect2 Lightning(float cX, float cY)
    {
        return new LightningEffect2(cX, cY);
    }

    public static GenericRenderEffect Pierce(AbstractCreature source, Hitbox target, float spread)
    {
        return Pierce(source, RandomX(target, spread), RandomY(target, spread));
    }

    public static GenericRenderEffect Pierce(AbstractCreature source, float cX, float cY)
    {
        final float x = cX + ((source.hb.cX > cX ? +80 : -80) * Settings.scale);
        final float rotation = Mathf.GetAngle(source.hb.cX, source.hb.cY, x, cY);
        JUtils.LogInfo(VFX.class, "Rotation:" + rotation);
        return new GenericRenderEffect(EYBEffect.IMAGES.Spear.Texture(), x, cY).SetRotation(rotation);
    }

    public static PsychokinesisEffect Psychokinesis(Hitbox target)
    {
        return Psychokinesis(target.cX, target.cY);
    }

    public static PsychokinesisEffect Psychokinesis(float cX, float cY)
    {
        return new PsychokinesisEffect(cX, cY);
    }

    public static RazorWindEffect RazorWind(Hitbox source, Hitbox target, float horizontalSpeed, float horizontalAcceleration)
    {
        return new RazorWindEffect(source.cX, source.cY, RandomY(target, 0.33f), horizontalSpeed, horizontalAcceleration);
    }

    public static RockBurstEffect RockBurst(Hitbox target, float scale)
    {
        return new RockBurstEffect(target, scale);
    }

    public static ShieldEffect Shield(Hitbox target)
    {
        return Shield(target.cX, target.cY);
    }

    public static ShieldEffect Shield(float cX, float cY)
    {
        return new ShieldEffect(cX, cY);
    }

    public static ShootingStarsEffect ShootingStars(Hitbox source, float spreadY)
    {
        return new ShootingStarsEffect(source.cX, source.cY).SetSpread(0, spreadY).FlipHorizontally(FlipHorizontally());
    }

    public static SnowballEffect Snowball(Hitbox source, Hitbox target)
    {
        return new SnowballEffect(source.cX, source.cY, target.cX, target.cY);
    }

    public static SnowballTriggerEffect SnowballTrigger(Hitbox source)
    {
        return SnowballTrigger(source.cX, source.cY);
    }

    public static SnowballTriggerEffect SnowballTrigger(float cX, float cY)
    {
        return (SnowballTriggerEffect) new SnowballTriggerEffect(cX, cY, 8).SetColor(Color.SKY.cpy());
    }

    public static ThrowDaggerEffect2 ThrowDagger(Hitbox target, float variance)
    {
        return new ThrowDaggerEffect2(RandomX(target, variance), RandomY(target, variance));
    }

    public static ThrowProjectileEffect ThrowProjectile(Projectile projectile, Hitbox target)
    {
        return new ThrowProjectileEffect(projectile, target);
    }

    public static ThrowProjectileEffect ThrowRock(Hitbox source, Hitbox target, float duration)
    {
        duration *= Mathf.Abs(target.cX - source.cX) / (Settings.WIDTH * 0.5f);
        return (ThrowProjectileEffect)new ThrowProjectileEffect(new Projectile(Earth.GetRandomTexture(), 128f, 128f)
                .SetColor(Colors.Random(0.8f, 1f, true))
                .SetPosition(source.cX, source.cY), target)
                .SetTargetRotation(36000, 360f)
                .AddCallback(hb -> GameEffects.Queue.Add(RockBurst(hb, 1.3f)))
                .SetDuration(duration, true);
    }

    public static VerticalImpactEffect VerticalImpact(Hitbox target)
    {
        return new VerticalImpactEffect(target.cX + target.width / 4f, target.cY - target.height / 4f);
    }

    public static WeightyImpactEffect WeightyImpact(Hitbox target)
    {
        return WeightyImpact(target, new Color(1.0F, 1.0F, 0.1F, 0.0F));
    }

    public static WeightyImpactEffect WeightyImpact(Hitbox target, Color color)
    {
        return new WeightyImpactEffect(target.cX, target.cY);
    }

    public static FadingParticleEffect Water(Hitbox target, float spread)
    {
        return Water(RandomX(target, spread), RandomY(target, spread));
    }

    public static FadingParticleEffect Water(float cX, float cY)
    {
        return (FadingParticleEffect) new FadingParticleEffect(EYBEffect.IMAGES.Water.Texture(), cX, cY).SetColor(Color.WHITE)
                .Edit(p -> p.SetRotation(MathUtils.random(100f,800f)).SetTargetRotation(36000).SetSpeed(0f, 0f, MathUtils.random(500f, 750f)).SetTargetScale(1f,5f))
                .SetTranslucent(MathUtils.random(0.7f,1f))
                .SetDuration(1.3f,false);
    }

    public static GenericAnimationEffect WaterDome(float cX, float cY)
    {
        return new GenericAnimationEffect(EYBEffect.IMAGES.WaterDome.Texture(), cX, cY, 12, 5, 0.015f).SetScale(2f);
    }

    public static GenericAnimationEffect Whack(Hitbox target, float spread)
    {
        return Whack(RandomX(target, spread), RandomY(target, spread));
    }

    public static GenericAnimationEffect Whack(float cX, float cY)
    {
        return new GenericAnimationEffect(EYBEffect.IMAGES.Whack.Texture(), cX, cY, 4, 4);
    }

    public static WhirlwindEffect Whirlwind()
    {
        return Whirlwind(new Color(0.9F, 0.9F, 1.0F, 1.0F), false);
    }

    public static WhirlwindEffect Whirlwind(Color color, boolean reverse)
    {
        return new WhirlwindEffect(color, reverse);
    }
}

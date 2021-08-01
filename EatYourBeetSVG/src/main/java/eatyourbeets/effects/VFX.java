package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.combat.*;
import eatyourbeets.effects.vfx.SmallLaserEffect;
import eatyourbeets.effects.vfx.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.Mathf;

public class VFX
{
    protected static float RandomX(Hitbox hb, float variance)
    {
        return hb.cX + (MathUtils.random(-variance, variance) * hb.width);
    }

    protected static float RandomY(Hitbox hb, float variance)
    {
        return hb.cY + (MathUtils.random(-variance, variance) * hb.height);
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

    public static ClawEffect Claw(Hitbox source, Color color1, Color color2)
    {
        return new ClawEffect(source.cX, source.cY, color1, color2);
    }

    public static MindblastEffect Mindblast(float dialogX, float dialogY, boolean flipHorizontal)
    {
        return new MindblastEffect(dialogX, dialogY, flipHorizontal);
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

    public static ExplosionSmallEffect SmallExplosion(Hitbox source)
    {
        return new ExplosionSmallEffect(source.cX, source.cY);
    }

    public static ExplosionSmallEffect SmallExplosion(Hitbox source, float variance)
    {
        return new ExplosionSmallEffect(RandomX(source, variance), RandomY(source, variance));
    }

    public static SmallLaserEffect SmallLaser(Hitbox source, Hitbox target, Color color)
    {
        return new SmallLaserEffect(source.cX, source.cY, RandomX(target, 0.2f), RandomY(target, 0.2f), color);
    }

    public static FallingIceEffect FallingIce(int frostCount)
    {
        return new FallingIceEffect(frostCount, FlipHorizontally());
    }

    public static FireballEffect2 Fireball(Hitbox source, Hitbox target)
    {
        return new FireballEffect2(source.cX, source.cY, target.cX, target.cY);
    }

    public static FlameBarrierEffect FlameBarrier(Hitbox source)
    {
        return new FlameBarrierEffect(source.cX, source.cY);
    }

    public static FlashAttackEffect FlashAttack(Hitbox target, AbstractGameAction.AttackEffect effect, boolean muteSFX)
    {
        return new FlashAttackEffect(target.cX, target.cY, effect, muteSFX);
    }

    public static ThrowDaggerEffect ThrowDagger(Hitbox target, float variance)
    {
        return new ThrowDaggerEffect(RandomX(target, variance), RandomY(target, variance));
    }

    public static ThrowProjectileEffect ThrowRock(Hitbox source, Hitbox target, float duration)
    {
        duration *= Mathf.Abs(target.cX - source.cX) / (Settings.WIDTH * 0.5f);
        return (ThrowProjectileEffect)new ThrowProjectileEffect(new Projectile(Earth.PROJECTILE_LARGE, 128f, 128f)
        .SetColor(Mathf.RandomColor(0.6f, 0.85f, true))
        .SetPosition(source.cX, source.cY), target)
        .AddCallback(hb -> GameEffects.Queue.Add(RockBurst(hb, 1.3f)))
        .SetDuration(duration, true);
    }

    public static ThrowProjectileEffect ThrowProjectile(Projectile projectile, Hitbox target)
    {
        return new ThrowProjectileEffect(projectile, target);
    }

    public static HemokinesisEffect2 Hemokinesis(Hitbox source, Hitbox target)
    {
        return new HemokinesisEffect2(target.cX, target.cY, source.cX, source.cY);
    }

    public static LightningEffect Lightning(Hitbox target)
    {
        return new LightningEffect(target.cX, target.cY);
    }

    public static RockBurstEffect RockBurst(Hitbox target, float scale)
    {
        return new RockBurstEffect(target.cX, target.cY, scale);
    }

    public static ShootingStarsEffect ShootingStars(Hitbox source, float spread)
    {
        return new ShootingStarsEffect(source.cX, source.cY, spread, FlipHorizontally());
    }

    public static SnowballEffect Snowball(Hitbox source, Hitbox target)
    {
        return new SnowballEffect(source.cX, source.cY, target.cX, target.cY);
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

    public static WhirlwindEffect Whirlwind()
    {
        return Whirlwind(new Color(0.9F, 0.9F, 1.0F, 1.0F), false);
    }

    public static WhirlwindEffect Whirlwind(Color color, boolean reverse)
    {
        return new WhirlwindEffect(color, reverse);
    }
}

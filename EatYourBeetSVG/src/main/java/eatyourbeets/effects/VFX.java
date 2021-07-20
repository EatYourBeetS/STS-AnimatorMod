package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.combat.*;
import eatyourbeets.effects.vfx.*;

public class VFX
{
    public static CataclysmEffect Cataclysm()
    {
        return new CataclysmEffect();
    }

    public static MeteorFallEffect MeteorFall(Hitbox hb)
    {
        return new MeteorFallEffect(hb.cX, hb.cY, MathUtils.random(-0.2f, 0.2f) * hb.width, MathUtils.random(-0.2f, 0.2f) * hb.height);
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
        return new BiteEffect2(target.cX, target.cY - 40.0F * Settings.scale, color);
    }

    public static ClawEffect Claw(Hitbox source, Color color1, Color color2)
    {
        return new ClawEffect(source.cX, source.cY, color1, color2);
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

    public static HemokinesisEffect2 Hemokinesis(Hitbox source, Hitbox target)
    {
        return new HemokinesisEffect2(target.cX, target.cY, source.cX, source.cY);
    }

    public static LightningEffect Lightning(Hitbox target)
    {
        return new LightningEffect(target.cX, target.cY);
    }

    public static RockEffect Rock(Hitbox source)
    {
        return new RockEffect(source.cX, source.cY);
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

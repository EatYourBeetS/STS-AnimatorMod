package eatyourbeets.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class DealDamageToAll extends EYBActionWithCallback<ArrayList<AbstractCreature>>
{
    public final int[] damage;

    protected final ArrayList<AbstractCreature> targets = new ArrayList<>();
    protected BiConsumer<AbstractCreature, Boolean> onDamageEffect;
    protected boolean bypassBlock;
    protected boolean bypassThorns;
    protected boolean isFast;

    protected Color vfxColor;
    protected float pitchMin;
    protected float pitchMax;

    public DealDamageToAll(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AttackEffect attackEffect)
    {
        this(source, amount, damageType, attackEffect, false);
    }

    public DealDamageToAll(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AttackEffect attackEffect, boolean isFast)
    {
        super(ActionType.DAMAGE, isFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.attackEffect = attackEffect;
        this.damageType = damageType;
        this.damage = amount;
        this.pitchMin = 0.95f;
        this.pitchMax = 1.05f;

        Initialize(source, null, amount[0]);
    }

    public DealDamageToAll SetDamageEffect(BiConsumer<AbstractCreature, Boolean> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamageToAll SetPiercing(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    public DealDamageToAll SetVFXColor(Color color)
    {
        this.vfxColor = color.cpy();

        return this;
    }

    public DealDamageToAll SetSoundPitch(float pitchMin, float pitchMax)
    {
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;

        return this;
    }

    public DealDamageToAll SetVFX(boolean superFast, boolean muteSfx)
    {
        this.isFast = superFast;

        if (muteSfx)
        {
            this.pitchMin = this.pitchMax = 0;
        }

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        boolean mute = pitchMin == 0;

        int i = 0;
        for (AbstractMonster enemy : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            if (!GameUtilities.IsDeadOrEscaped(enemy))
            {
                GameEffects.List.Attack(source, enemy, this.attackEffect, pitchMin, pitchMax, vfxColor);

                if (onDamageEffect != null)
                {
                    onDamageEffect.accept(enemy, !mute);
                }

                mute = true;
            }

            i += 1;
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            for (AbstractPower p : player.powers)
            {
                p.onDamageAllEnemies(this.damage);
            }

            int i = 0;
            for (AbstractMonster enemy : GameUtilities.GetEnemies(false))
            {
                if (!GameUtilities.IsDeadOrEscaped(enemy))
                {
                    DamageHelper.DealDamage(enemy, new DamageInfo(this.source, this.damage[i], this.damageType), attackEffect, bypassBlock, bypassThorns);
                    targets.add(enemy);
                }

                i += 1;
            }

            if (GameUtilities.GetCurrentRoom(true).monsters.areMonstersBasicallyDead())
            {
                GameUtilities.ClearPostCombatActions();
            }

            if (!isFast && !Settings.FAST_MODE)
            {
                GameActions.Top.Wait(0.1f);
            }

            Complete(targets);
        }
    }
}

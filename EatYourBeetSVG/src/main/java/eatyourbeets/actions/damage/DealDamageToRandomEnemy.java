package eatyourbeets.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.*;

public class DealDamageToRandomEnemy extends EYBActionWithCallback<AbstractCreature>
{
    protected boolean hasPlayedEffect;
    protected boolean applyPowers;
    protected boolean bypassBlock;
    protected boolean bypassThorns;
    protected boolean skipWait;
    protected boolean canKill;
    protected boolean isOrb;

    protected Color vfxColor = null;
    protected Color enemyTint = null;
    protected float pitchMin = 0.95f;
    protected float pitchMax = 1.05f;

    protected final DamageInfo info;
    protected FuncT1<Float, AbstractCreature> onDamageEffect;
    protected GenericCondition<AbstractCreature> filter;

    protected DealDamageToRandomEnemy(DealDamageToRandomEnemy other)
    {
        this(other.info, other.attackEffect);

        Import(other);

        this.card = other.card;
        this.isOrb = other.isOrb;
        this.vfxColor = other.vfxColor;
        this.enemyTint = other.enemyTint;
        this.pitchMin = other.pitchMin;
        this.pitchMax = other.pitchMax;
        this.skipWait = other.skipWait;
        this.canKill = other.canKill;
        this.applyPowers = other.applyPowers;
        this.bypassBlock = other.bypassBlock;
        this.bypassThorns = other.bypassThorns;
        this.filter = other.filter;
        this.onDamageEffect = other.onDamageEffect;
        this.hasPlayedEffect = other.hasPlayedEffect;
    }

    public DealDamageToRandomEnemy(AbstractCard card, AttackEffect effect)
    {
        super(ActionType.DAMAGE);

        this.applyPowers = true;
        this.card = card;
        this.info = new DamageInfo(player, card.baseDamage, card.damageTypeForTurn);
        this.attackEffect = effect;
        this.canKill = true;

        Initialize(player, null, info.output);
    }

    public DealDamageToRandomEnemy(DamageInfo info, AttackEffect effect)
    {
        super(ActionType.DAMAGE);

        this.applyPowers = true;
        this.card = null;
        this.info = info;
        this.attackEffect = effect;
        this.canKill = true;

        Initialize(player, null, info.output);
    }

    public DealDamageToRandomEnemy CanKill(boolean canKill)
    {
        this.canKill = canKill;

        return this;
    }

    public DealDamageToRandomEnemy SetDamageEffect(FuncT1<Float, AbstractCreature> onDamageEffect)
    {
        this.onDamageEffect = onDamageEffect;

        return this;
    }

    public DealDamageToRandomEnemy SetPiercing(boolean bypassThorns, boolean bypassBlock)
    {
        this.bypassBlock = bypassBlock;
        this.bypassThorns = bypassThorns;

        return this;
    }

    public DealDamageToRandomEnemy SetVFXColor(Color color)
    {
        this.vfxColor = color.cpy();

        return this;
    }

    public DealDamageToRandomEnemy SetVFXColor(Color color, Color enemyTint)
    {
        this.vfxColor = color.cpy();
        this.enemyTint = enemyTint.cpy();

        return this;
    }

    public DealDamageToRandomEnemy SetSoundPitch(float pitch)
    {
        return SetSoundPitch(pitch, pitch);
    }

    public DealDamageToRandomEnemy SetSoundPitch(float pitchMin, float pitchMax)
    {
        this.pitchMin = pitchMin;
        this.pitchMax = pitchMax;

        return this;
    }

    public DealDamageToRandomEnemy SetOptions(boolean superFast, boolean isOrb)
    {
        this.skipWait = superFast;
        this.isOrb = isOrb;
        this.applyPowers = true;

        return this;
    }

    public DealDamageToRandomEnemy SetOptions(boolean superFast, boolean isOrb, boolean applyPowers)
    {
        this.applyPowers = applyPowers;
        this.skipWait = superFast;
        this.isOrb = isOrb;

        return this;
    }

    public DealDamageToRandomEnemy SetFilter(FuncT1<Boolean, AbstractCreature> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> DealDamageToRandomEnemy SetFilter(S state, FuncT2<Boolean, S, AbstractCreature> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return this.target == null || (this.source != null && this.source.isDying);
    }

    @Override
    protected void FirstUpdate()
    {
        target = FindTarget();

        if (shouldCancelAction())
        {
            Complete();
            return;
        }

        if (onDamageEffect != null)
        {
            AddDuration(onDamageEffect.Invoke(target));
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (shouldCancelAction())
        {
            Complete();
            return;
        }

        if (!hasPlayedEffect && duration < 0.1f)
        {
            if (GameUtilities.IsValidTarget(target))
            {
                AddDuration(AttackEffects.GetDamageDelay(attackEffect));
                GameEffects.List.Attack(source, target, attackEffect, pitchMin, pitchMax, vfxColor);
            }

            hasPlayedEffect = true;
        }

        if (TickDuration(deltaTime))
        {
            if (applyPowers)
            {
                if (card != null)
                {
                    card.calculateCardDamage((AbstractMonster) target);
                    this.info.output = card.damage;
                }
                else
                {
                    this.info.applyPowers(this.info.owner, target);
                }
            }

            if (isOrb)
            {
                this.info.output = AbstractOrb.applyLockOn(target, this.info.output);
            }

            DamageHelper.ApplyTint(target, enemyTint, attackEffect);
            DamageHelper.DealDamage(target, info, bypassBlock, bypassThorns, canKill);

            if (GameUtilities.AreMonstersBasicallyDead())
            {
                GameUtilities.ClearPostCombatActions();
            }

            if (!Settings.FAST_MODE && !skipWait)
            {
                GameActions.Top.Wait(0.1f);
            }

            Complete(target);
        }
    }

    protected AbstractCreature FindTarget()
    {
        final RandomizedList<AbstractCreature> enemies = new RandomizedList<>(GameUtilities.GetEnemies(true));
        while (enemies.Size() > 0)
        {
            final AbstractCreature enemy = enemies.Retrieve(rng);
            if (GameUtilities.IsValidTarget(enemy) && (filter == null || filter.Check(enemy)))
            {
                return enemy;
            }
        }

        return null;
    }
}

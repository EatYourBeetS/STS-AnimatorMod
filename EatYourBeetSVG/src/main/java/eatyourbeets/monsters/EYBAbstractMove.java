package eatyourbeets.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public abstract class EYBAbstractMove
{
    public final int ascensionLevel;

    public AbstractGameAction.AttackEffect attackEffect;
    public AbstractCreature.CreatureAnimation attackAnimation;
    public ActionT2<EYBAbstractMove, AbstractCreature> onUse;
    public FuncT1<Boolean, EYBAbstractMove> canUse;
    public AbstractMonster.Intent intent;
    public DamageInfo damageInfo;
    public AbstractMonster owner;
    public String name;
    public float ascensionBonus;
    public boolean disabled;
    public int amount;
    public int damageAmount;
    public int blockAmount;
    public int damageMultiplier;
    public int uses = -1;
    public byte id;

    public EYBAbstractMove()
    {
        this.ascensionLevel = GameUtilities.GetAscensionLevel();
    }

    public int CalculateAscensionBonus(int base, float percentage)
    {
        return Math.round(base * percentage * (ascensionLevel / 20f));
    }

    public boolean CanUse(Byte previousMove)
    {
        return canUse != null ? canUse.Invoke(this) : (!disabled && previousMove != id && uses != 0);
    }

    public void Execute(AbstractPlayer target)
    {
        if (uses > 0)
        {
            uses -= 1;
        }

        if (onUse != null)
        {
            onUse.Invoke(this, target);
        }
        else
        {
            QueueActions(target);
        }
    }

    public void Initialize(byte id, AbstractMonster owner)
    {
        this.owner = owner;
        this.id = id;

        if (this.damageInfo != null)
        {
            this.damageInfo.owner = owner;
        }
    }

    public void QueueActions(AbstractCreature target)
    {
        if (damageInfo != null)
        {
            if (attackAnimation == null)
            {
                attackAnimation = AbstractCreature.CreatureAnimation.ATTACK_FAST;
            }
            if (attackEffect == null)
            {
                attackEffect = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
            }

            UseAnimation(attackAnimation);
            damageInfo.applyPowers(owner, target);
            GameActions.Bottom.Add(new DamageAction(target, damageInfo, attackEffect));
        }
    }

    public void Select()
    {
        if (damageInfo != null)
        {
            owner.setMove(name, id, intent, damageInfo.base, damageMultiplier, damageMultiplier > 1);
        }
        else
        {
            owner.setMove(name, id, intent);
        }
    }

    public EYBAbstractMove SetAscensionBonus(float ascensionBonus)
    {
        this.ascensionBonus = ascensionBonus;

        return this;
    }

    public EYBAbstractMove SetCanUse(FuncT1<Boolean, EYBAbstractMove> canUse)
    {
        this.canUse = canUse;

        return this;
    }

    public EYBAbstractMove SetDamage(int damage)
    {
        this.damageInfo = new DamageInfo(owner, damage);
        this.damageMultiplier = 1;

        return this;
    }

    public EYBAbstractMove SetDamage(int damage, int multiplier)
    {
        this.damageInfo = new DamageInfo(owner, damage);
        this.damageMultiplier = multiplier;

        return this;
    }

    public EYBAbstractMove SetDamageEffect(AbstractGameAction.AttackEffect attackEffect, AbstractCreature.CreatureAnimation attackAnimation)
    {
        this.attackEffect = attackEffect;
        this.attackAnimation = attackAnimation;

        return this;
    }

    public EYBAbstractMove SetDisabled(boolean disabled)
    {
        this.disabled = disabled;

        return this;
    }

    public EYBAbstractMove SetIntent(AbstractMonster.Intent intent)
    {
        this.intent = intent;

        return this;
    }

    public EYBAbstractMove SetOnUse(ActionT2<EYBAbstractMove, AbstractCreature> onUse)
    {
        this.onUse = onUse;

        return this;
    }

    public EYBAbstractMove SetUses(int uses)
    {
        this.uses = uses;

        return this;
    }

    public void UseAnimation(AbstractCreature.CreatureAnimation animation)
    {
        switch (animation)
        {
            case FAST_SHAKE:
                owner.useFastShakeAnimation(Settings.ACTION_DUR_FAST);
                break;

            case SHAKE:
                owner.useShakeAnimation(Settings.ACTION_DUR_MED);
                break;

            case ATTACK_FAST:
                owner.useFastAttackAnimation();
                break;

            case ATTACK_SLOW:
                owner.useSlowAttackAnimation();
                break;

            case STAGGER:
                owner.useStaggerAnimation();
                break;

            case HOP:
                owner.useHopAnimation();
                break;

            case JUMP:
                owner.useJumpAnimation();
                break;
        }
    }
}

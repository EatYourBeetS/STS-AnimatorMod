package eatyourbeets.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.PowerTarget;

import java.util.ArrayList;

public abstract class EYBAbstractMove
{
    public static class PowerTemplate
    {
        public final PowerHelper power;
        public final PowerTarget target;
        public final Integer amount;

        public PowerTemplate(PowerHelper power, PowerTarget target, Integer amount)
        {
            this.power = power;
            this.target = target;
            this.amount = amount;
        }
    }

    public byte id;
    public String name;

    public final int ascensionLevel;
    public final ArrayList<PowerTemplate> powerTemplates = new ArrayList<>();
    public AbstractGameAction.AttackEffect attackEffect;
    public AbstractCreature.CreatureAnimation attackAnimation;
    public ActionT2<EYBAbstractMove, AbstractCreature> onUse;
    public FuncT2<Boolean, EYBAbstractMove, Byte> canUse;
    public ActionT1<EYBAbstractMove> onSelect;
    public AbstractMonster.Intent intent;
    public PowerTarget powerTarget = PowerTarget.Source;
    public AbstractMonster owner;
    public DamageInfo damageInfo;
    public boolean disabled;
    public int damageMultiplier;
    public boolean shieldAll;
    public int uses = -1;
    public Object data;

    public MoveAttribute damage;
    public MoveAttribute block;
    public MoveAttribute misc;

    public EYBAbstractMove()
    {
        this.ascensionLevel = GameUtilities.GetAscensionLevel();
    }

    public int CalculateAscensionBonus(int base, float percentage)
    {
        return Math.round(base * percentage * (ascensionLevel / 20f));
    }

    public boolean CanUseFallback(Byte previousMove)
    {
        return !disabled && previousMove != id && uses != 0;
    }

    public boolean CanUse(Byte previousMove)
    {
        return canUse != null ? canUse.Invoke(this, previousMove) : CanUseFallback(previousMove);
    }

    public void Execute(AbstractPlayer target)
    {
        if (uses > 0)
        {
            uses -= 1;
        }

        QueueActions(target);

        if (onUse != null)
        {
            onUse.Invoke(this, target);
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
        if (block != null)
        {
            block.Calculate();

            if (shieldAll)
            {
                for (AbstractMonster e : GameUtilities.GetAllEnemies(true))
                {
                    GameActions.Bottom.Add(new GainBlockAction(e, owner, block.amount, true));
                }
            }
            else
            {
                GameActions.Bottom.GainBlock(owner, block.amount);
            }
        }

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
            if (damageMultiplier < 1)
            {
                damageMultiplier = 1;
            }

            UseAnimation(attackAnimation);
            damageInfo.base = damage.Calculate();
            damageInfo.applyPowers(owner, target);

            for (int i = 0; i < damageMultiplier; i++)
            {
                GameActions.Bottom.Add(new DamageAction(target, damageInfo, attackEffect));
            }
        }

        for (PowerTemplate template : powerTemplates)
        {
            int amount = template.amount != null ? template.amount : misc.Calculate();
            PowerTarget powerTarget = template.target != null ? template.target : this.powerTarget;
            powerTarget.ApplyPowers(template.power, owner, target, amount);
        }
    }

    public void Select()
    {
        if (damageInfo != null)
        {
            damageInfo.base = damage.Calculate();
            owner.setMove(name, id, intent, damageInfo.base, damageMultiplier, damageMultiplier > 1);
        }
        else
        {
            owner.setMove(name, id, intent);
        }

        if (onSelect != null)
        {
            onSelect.Invoke(this);
        }
    }

    public EYBAbstractMove SetCanUse(FuncT2<Boolean, EYBAbstractMove, Byte> canUse)
    {
        this.canUse = canUse;

        return this;
    }

    public EYBAbstractMove SetData(Object data)
    {
        this.data = data;

        return this;
    }

    public EYBAbstractMove SetPowerTarget(PowerTarget target)
    {
        this.powerTarget = target;

        return this;
    }

    public EYBAbstractMove AddPower(PowerHelper power)
    {
        this.powerTemplates.add(new PowerTemplate(power, null, null));

        return this;
    }

    public EYBAbstractMove AddPower(PowerHelper power, int amount)
    {
        this.powerTemplates.add(new PowerTemplate(power, null, amount));

        return this;
    }

    public EYBAbstractMove AddPower(PowerHelper power, PowerTarget powerTarget, int amount)
    {
        this.powerTemplates.add(new PowerTemplate(power, powerTarget, amount));

        return this;
    }

    public EYBAbstractMove SetBlockAoE(boolean shieldAll)
    {
        this.shieldAll = shieldAll;

        return this;
    }

    public EYBAbstractMove SetMisc(int amount)
    {
        this.misc = new MoveAttribute(amount);

        return this;
    }

    public EYBAbstractMove SetMiscBonus(int minAscension, int bonus)
    {
        this.misc.ascensionThreshold = minAscension;
        this.misc.ascensionThresholdBonus = bonus;

        return this;
    }

    public EYBAbstractMove SetMiscScaling(float scaling)
    {
        this.misc.ascensionScaling = scaling;

        return this;
    }

    public EYBAbstractMove SetBlock(int block)
    {
        this.block = new MoveAttribute(block);

        return this;
    }

    public EYBAbstractMove SetBlockBonus(int minAscension, int bonus)
    {
        this.block.ascensionThreshold = minAscension;
        this.block.ascensionThresholdBonus = bonus;

        return this;
    }

    public EYBAbstractMove SetBlockScaling(float scaling)
    {
        this.block.ascensionScaling = scaling;

        return this;
    }

    public EYBAbstractMove SetAttackEffect(AbstractGameAction.AttackEffect attackEffect, AbstractCreature.CreatureAnimation attackAnimation)
    {
        this.attackEffect = attackEffect;
        this.attackAnimation = attackAnimation;

        return this;
    }

    public EYBAbstractMove SetDamage(int damage, int multiplier)
    {
        this.damage = new MoveAttribute(damage);
        this.damageInfo = new DamageInfo(owner, damage);
        this.damageMultiplier = multiplier;

        return this;
    }

    public EYBAbstractMove SetDamageMultiplier(int multiplier)
    {
        this.damageMultiplier = multiplier;

        return this;
    }

    public EYBAbstractMove SetDamageBonus(int minAscension, int bonus)
    {
        this.damage.ascensionThreshold = minAscension;
        this.damage.ascensionThresholdBonus = bonus;

        return this;
    }

    public EYBAbstractMove SetDamageScaling(float scaling)
    {
        this.damage.ascensionScaling = scaling;

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

    public EYBAbstractMove SetOnSelect(ActionT1<EYBAbstractMove> onSelect)
    {
        this.onSelect = onSelect;

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

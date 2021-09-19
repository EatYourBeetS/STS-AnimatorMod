package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.JUtils;

public class TemptationPower extends CommonPower {
    public static final String POWER_ID = CreateFullID(TemptationPower.class);
    public static final float MULTIPLIER = 10f;
    private float percentage;

    public TemptationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }


    @Override
    public void updateDescription()
    {
        UpdatePercentage();

        this.description = FormatDescription(0, this.percentage * 100F, amount);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        UpdatePercentage();
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        UpdatePercentage();
    }

    public void UpdatePercentage()
    {
        this.percentage = MULTIPLIER * this.amount / 100f;
        CombatStats.EnemyFrailModifier += this.percentage;
        CombatStats.EnemyLockOnModifier += this.percentage;
        CombatStats.EnemyVulnerableModifier += this.percentage;
        CombatStats.EnemyWeakModifier += this.percentage;
        CombatStats.PlayerFrailModifier += this.percentage;
        CombatStats.PlayerVulnerableModifier += this.percentage;
        CombatStats.PlayerWeakModifier += this.percentage;
        CombatStats.EnemyFrailModifier += this.percentage;
        CombatStats.EnemyLockOnModifier += this.percentage;
        CombatStats.EnemyVulnerableModifier += this.percentage;
        CombatStats.EnemyWeakModifier += this.percentage;
        CombatStats.PlayerFrailModifier += this.percentage;
        CombatStats.PlayerVulnerableModifier += this.percentage;
        CombatStats.PlayerWeakModifier += this.percentage;
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (power.ID.equals(PoisonPower.POWER_ID) || power.ID.equals(BurningPower.POWER_ID) || power.ID.equals(DelayedDamagePower.POWER_ID))
        {
            power.amount += this.amount;

            final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount += this.amount;
            }
            else
            {
                JUtils.LogWarning(this, "Unknown action type: " + action.getClass().getName());
            }
        }
    }
}

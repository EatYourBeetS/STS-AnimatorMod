package eatyourbeets.powers.common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;

public class BalancePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(BalancePower.class);
    public static final int MULTIPLIER = 10;
    private float percentage;

    public BalancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;
        this.percentage = 0;

        Initialize(amount);
        UpdatePercentage();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    private int calculateMultiplier(float value, float baseMultiplier) {
        float modifiedMultiplier = (1.0f - baseMultiplier) * percentage;
        float finalMultiplier = Math.max(0, 1 - modifiedMultiplier);
        return MathUtils.ceil(value * finalMultiplier / baseMultiplier);
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
        CombatStats.EnemyFrailModifier -= this.percentage;
        CombatStats.EnemyLockOnModifier -= this.percentage;
        CombatStats.EnemyVulnerableModifier -= this.percentage;
        CombatStats.EnemyWeakModifier -= this.percentage;
        CombatStats.PlayerFrailModifier -= this.percentage;
        CombatStats.PlayerVulnerableModifier -= this.percentage;
        CombatStats.PlayerWeakModifier -= this.percentage;
        this.percentage = MULTIPLIER * this.amount / 100f;
        CombatStats.EnemyFrailModifier += this.percentage;
        CombatStats.EnemyLockOnModifier += this.percentage;
        CombatStats.EnemyVulnerableModifier += this.percentage;
        CombatStats.EnemyWeakModifier += this.percentage;
        CombatStats.PlayerFrailModifier += this.percentage;
        CombatStats.PlayerVulnerableModifier += this.percentage;
        CombatStats.PlayerWeakModifier += this.percentage;
    }
}

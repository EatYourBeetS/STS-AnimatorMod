package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.replacement.AnimatorFrailPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.powers.replacement.AnimatorWeakPower;

public class EndurancePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(EndurancePower.class);
    public static final int MULTIPLIER = 10;
    public static final int MULTIPLIER2 = 5;
    public static final int BLOCK_MULTIPLIER = 2;
    private int totalMultiplier = 0;
    private int totalMultiplier2 = 0;

    public EndurancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        UpdatePercentage();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(amount >= 0 ? 0 : 1, totalMultiplier, totalMultiplier2, amount * BLOCK_MULTIPLIER);
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

    @Override
    public void onRemove()
    {
        this.amount = 0;
        UpdatePercentage();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (isPlayer)
        {
            CombatStats.BlockRetained = Math.max(0,CombatStats.BlockRetained + amount * BLOCK_MULTIPLIER);
        }
    }

    public void UpdatePercentage()
    {
        //Undo the previous changes made by this power
        AnimatorVulnerablePower.AddPlayerModifier(this.totalMultiplier);
        AnimatorWeakPower.AddPlayerModifier(this.totalMultiplier2);
        AnimatorFrailPower.AddPlayerModifier(this.totalMultiplier2);
        ImpairedPower.AddPlayerModifier(this.totalMultiplier);

        this.totalMultiplier = MULTIPLIER * this.amount;
        this.totalMultiplier2 = MULTIPLIER2 * this.amount;

        AnimatorVulnerablePower.AddPlayerModifier(-this.totalMultiplier);
        AnimatorWeakPower.AddPlayerModifier(-this.totalMultiplier2);
        AnimatorFrailPower.AddPlayerModifier(-this.totalMultiplier2);
        ImpairedPower.AddPlayerModifier(-this.totalMultiplier);
    }
}

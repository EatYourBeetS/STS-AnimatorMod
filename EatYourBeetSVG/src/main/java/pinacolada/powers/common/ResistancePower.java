package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.powers.replacement.PCLFrailPower;
import pinacolada.powers.replacement.PCLVulnerablePower;
import pinacolada.powers.replacement.PCLWeakPower;

public class ResistancePower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(ResistancePower.class);
    public static final int MULTIPLIER = 5;
    public static final int MULTIPLIER2 = 3;
    private int totalMultiplier = 0;
    private int totalMultiplier2 = 0;

    public ResistancePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
        this.canGoNegative = true;
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
        this.description = FormatDescription(amount >= 0 ? 0 : 1, Math.abs(totalMultiplier), Math.abs(totalMultiplier2));
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
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        if (amount == 0) {
            RemovePower();
        }
    }

    public void UpdatePercentage()
    {
        //Undo the previous changes made by this power
        ImpairedPower.AddPlayerModifier(this.totalMultiplier);
        PCLVulnerablePower.AddPlayerModifier(this.totalMultiplier);
        PCLWeakPower.AddPlayerModifier(this.totalMultiplier2);
        PCLFrailPower.AddPlayerModifier(this.totalMultiplier2);

        this.totalMultiplier = MULTIPLIER * this.amount;
        this.totalMultiplier2 = MULTIPLIER2 * this.amount;

        ImpairedPower.AddPlayerModifier(-this.totalMultiplier);
        PCLVulnerablePower.AddPlayerModifier(-this.totalMultiplier);
        PCLWeakPower.AddPlayerModifier(-this.totalMultiplier2);
        PCLFrailPower.AddPlayerModifier(-this.totalMultiplier2);
    }
}
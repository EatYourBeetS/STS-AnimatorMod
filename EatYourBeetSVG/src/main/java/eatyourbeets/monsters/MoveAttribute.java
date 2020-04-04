package eatyourbeets.monsters;

import eatyourbeets.utilities.GameUtilities;

public class MoveAttribute
{
    public transient int amount;

    public int baseAmount;
    public int ascensionThreshold;
    public int ascensionThresholdBonus;
    public float ascensionScaling;

    public MoveAttribute(int amount)
    {
        this.baseAmount = amount;
    }

    public MoveAttribute(int amount, int ascensionThreshold, int bonus)
    {
        this.baseAmount = amount;
        this.ascensionThreshold = ascensionThreshold;
        this.ascensionThresholdBonus = bonus;
    }

    public MoveAttribute(int amount, float ascensionScaling)
    {
        this.baseAmount = amount;
        this.ascensionScaling = ascensionScaling;
    }

    public int Calculate()
    {
        amount = baseAmount;

        int ascension = GameUtilities.GetAscensionLevel();
        if (ascension >= ascensionThreshold)
        {
            amount += ascensionThresholdBonus;
        }
        if (ascensionScaling > 0)
        {
            amount += Math.round(amount * ascensionScaling * (ascension / 20f));
        }

        return amount;
    }
}

package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;

public class UltimateCrystalPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UltimateCrystalPower.class.getSimpleName());

    private static final int SUMMON_COUNT = 2;

    public UltimateCrystalPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + SUMMON_COUNT + desc[1];
    }
}

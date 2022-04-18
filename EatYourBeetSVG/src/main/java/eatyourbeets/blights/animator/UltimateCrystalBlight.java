package eatyourbeets.blights.animator;

import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.utilities.GameActions;

public class UltimateCrystalBlight extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCrystalBlight.class);

    public UltimateCrystalBlight()
    {
        super(ID, IsUnnamedReign() ? 3 : 2);

        this.counter = -1;
    }

    @Override
    public String GetUpdatedDescription()
    {
        return FormatDescription(0, initialAmount);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        final int amount = IsUnnamedReign() ? 3 : 2;
        if (amount != initialAmount)
        {
            initialAmount = amount;
            updateDescription();
        }

        GameActions.Bottom.ReshuffleFromHand(name, initialAmount, false);
        this.flash();
    }
}
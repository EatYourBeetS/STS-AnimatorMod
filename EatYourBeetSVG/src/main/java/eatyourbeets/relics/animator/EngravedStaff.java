package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class EngravedStaff extends AnimatorRelic
{
    public static final String ID = CreateFullID(EngravedStaff.class.getSimpleName());
    public static final int RANDOM_STAT_AMOUNT = 1;

    public EngravedStaff()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(RANDOM_STAT_AMOUNT);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.GainRandomStat(RANDOM_STAT_AMOUNT);

        this.flash();
    }
}
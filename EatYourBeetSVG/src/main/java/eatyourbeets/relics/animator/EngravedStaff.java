package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class EngravedStaff extends AnimatorRelic
{
    public static final String ID = CreateFullID(EngravedStaff.class);

    public EngravedStaff()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.GainRandomStat(1, true);
        flash();
    }
}
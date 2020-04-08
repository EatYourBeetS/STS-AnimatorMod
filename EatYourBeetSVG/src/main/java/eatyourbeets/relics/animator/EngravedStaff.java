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
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        GameActions.Bottom.Callback(() ->
        {
            GameActions.Bottom.GainRandomStat(1, true);
            flash();
        });
    }
}
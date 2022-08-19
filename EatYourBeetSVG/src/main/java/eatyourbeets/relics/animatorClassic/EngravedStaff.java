package eatyourbeets.relics.animatorClassic;

import eatyourbeets.relics.AnimatorClassicRelic;
import eatyourbeets.utilities.GameActions;

public class EngravedStaff extends AnimatorClassicRelic
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
            GameActions.Bottom.GainRandomAffinityPower(1, true);
            flash();
        });
    }
}
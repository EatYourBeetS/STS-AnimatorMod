package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Buoy extends AnimatorRelic
{
    public static final String ID = CreateFullID(Buoy.class);
    public static final int RECOVER_AMOUNT = 2;

    public Buoy()
    {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(DESCRIPTIONS[0], RECOVER_AMOUNT);
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        GameActions.Bottom.RecoverHP(RECOVER_AMOUNT)
        .AddCallback(pair ->
        {
            if (pair.V2 > 0)
            {
                flash();
            }
        });
    }
}
package eatyourbeets.relics.animator;

import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SoulGem extends AnimatorRelic implements Hidden
{
    public static final String ID = CreateFullID(SoulGem.class);

    public SoulGem()
    {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, true, true)
        .SetFilter(GameUtilities::IsHindrance);

        flash();
    }
}
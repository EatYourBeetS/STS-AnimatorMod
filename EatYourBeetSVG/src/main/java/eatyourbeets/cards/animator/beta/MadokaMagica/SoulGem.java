package eatyourbeets.relics.animator;

import eatyourbeets.actions.handSelection.ExhaustFromHand;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class SoulGem extends AnimatorRelic {
    public static final String ID = CreateFullID(SoulGem.class.getSimpleName());

    public SoulGem() {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, true, true)
        .SetFilter(c -> GameUtilities.IsCurseOrStatus(c));
    }
}
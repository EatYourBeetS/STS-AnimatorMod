package eatyourbeets.relics.animator;

import eatyourbeets.orbs.Fire;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class AlchemistGlove extends AnimatorRelic
{
    public static final String ID = CreateFullID(AlchemistGlove.class.getSimpleName());

    public AlchemistGlove()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActions.Bottom.ChannelOrb(new Fire(), false);
    }
}
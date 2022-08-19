package eatyourbeets.relics.animatorClassic;

import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.relics.AnimatorClassicRelic;
import eatyourbeets.utilities.GameActions;

public class AlchemistGlove extends AnimatorClassicRelic
{
    public static final String ID = CreateFullID(AlchemistGlove.class);

    public AlchemistGlove()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActions.Bottom.ChannelOrb(new Fire());
    }
}
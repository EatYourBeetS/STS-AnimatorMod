package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Chaos extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Chaos.class, GR.Tooltips.Chaos)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Chaos()
    {
        super(DATA, Chaos.ORB_ID);

        SetAffinity_Star(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Chaos());
    }
}
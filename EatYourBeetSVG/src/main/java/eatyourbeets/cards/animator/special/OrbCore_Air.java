package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Air extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Air.class, GR.Tooltips.Air)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Air()
    {
        super(DATA, Air.ORB_ID);

        SetAffinity_Air(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Air());
    }
}
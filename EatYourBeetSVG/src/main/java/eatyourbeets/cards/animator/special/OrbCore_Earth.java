package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Earth extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Earth.class, GR.Tooltips.Earth)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Earth()
    {
        super(DATA, 5);

        SetAffinity_Earth(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Earth());
    }
}
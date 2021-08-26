package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Water extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Water.class, GR.Tooltips.Water)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Water()
    {
        super(DATA, 5);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Water());
    }
}
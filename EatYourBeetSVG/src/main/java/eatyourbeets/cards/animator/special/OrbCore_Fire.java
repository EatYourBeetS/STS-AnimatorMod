package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Fire extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Fire.class, GR.Tooltips.Fire)
            .SetPower(0, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Fire()
    {
        super(DATA, 4);

        SetAffinity_Fire(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Fire());
    }
}
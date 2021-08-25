package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Aether extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Aether.class, GR.Tooltips.Aether)
            .SetPower(0, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Aether()
    {
        super(DATA, 6);

        SetAffinity_Green(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Aether());
    }
}
package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Earth extends OrbCore implements Hidden
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Earth.class, GR.Tooltips.Earth)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Earth()
    {
        super(DATA, 3);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Earth());
    }
}
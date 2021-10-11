package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Plasma extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Plasma.class, GR.Tooltips.Plasma)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Plasma()
    {
        super(DATA, 6);

        SetAffinity_Earth(2);
        SetAffinity_Air(2);
        SetAffinity_Light(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Plasma());
    }
}
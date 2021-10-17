package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Plasma extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Plasma.class, GR.Tooltips.Plasma)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Plasma()
    {
        super(DATA, Plasma.ORB_ID);

        SetAffinity_Cyber(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Plasma());
    }
}
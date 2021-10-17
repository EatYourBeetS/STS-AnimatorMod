package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Lightning extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Lightning.class, GR.Tooltips.Lightning)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Lightning()
    {
        super(DATA, Lightning.ORB_ID);

        SetAffinity_Thunder(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Lightning());
    }
}
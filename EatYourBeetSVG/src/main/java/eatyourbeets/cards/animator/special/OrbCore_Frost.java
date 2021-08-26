package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Frost extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Frost.class, GR.Tooltips.Frost)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Frost()
    {
        super(DATA, 4);

        SetAffinity_Blue(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Frost());
    }
}
package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Dark extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Dark.class, GR.Tooltips.Dark)
            .SetPower(0, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Dark()
    {
        super(DATA, 3);

        SetAffinity_Dark(2);
    }

    public void ChannelOrb()
    {
        GameActions.Bottom.ChannelOrb(new Dark());
    }
}
package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.resources.GR;

public class OrbCore_Air extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Air.class, GR.Tooltips.Air, GR.Tooltips.Velocity, GR.Tooltips.Affinity_Green)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Air()
    {
        super(DATA, 1,6);

        SetAffinity_Green(2);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Air.class;
    }
}
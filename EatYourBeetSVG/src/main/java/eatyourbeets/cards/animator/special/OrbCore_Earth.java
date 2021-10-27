package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.resources.GR;

public class OrbCore_Earth extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Earth.class, GR.Tooltips.Earth, GR.Tooltips.Willpower, GR.Tooltips.Affinity_Orange)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Earth()
    {
        super(DATA, 1, 6);

        SetAffinity_Orange(2);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Earth.class;
    }
}
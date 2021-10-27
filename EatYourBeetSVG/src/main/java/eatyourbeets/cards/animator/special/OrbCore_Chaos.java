package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.resources.GR;

public class OrbCore_Chaos extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Chaos.class, GR.Tooltips.Chaos, GR.Tooltips.Affinity_Star, GR.Tooltips.Affinity_General)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Chaos()
    {
        super(DATA, 1, 9);

        SetAffinity_Star(2);
    }

    @Override
    public Affinity GetAffinity() {
        return Affinity.General;
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Chaos.class;
    }
}
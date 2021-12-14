package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;

public class OrbCore_Plasma extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Plasma.class, GR.Tooltips.Plasma, GR.Tooltips.Technic, GR.Tooltips.Affinity_Silver)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Plasma()
    {
        super(DATA, 1, 4);

        SetAffinity_Silver(2);
        SetAffinity_Light(2);
    }

    @Override
    public Affinity GetAffinity() {
        return Affinity.Silver;
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Plasma.class;
    }
}
package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;

public class OrbCore_Plasma extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Plasma.class, GR.Tooltips.Plasma, GR.Tooltips.Technic, GR.Tooltips.Affinity_Silver)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Plasma()
    {
        super(DATA, 1, 4);

        SetAffinity_Silver(1);
        SetAffinity_Light(1);
    }

    @Override
    public PCLAffinity GetAffinity() {
        return PCLAffinity.Silver;
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Plasma.class;
    }
}
package pinacolada.cards.pcl.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;

public class OrbCore_Plasma extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Plasma.class, PCLOrbHelper.Plasma)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Plasma()
    {
        super(DATA, 1, 4);

        SetAffinity_Silver(1);
        SetAffinity_Light(1);
    }
}
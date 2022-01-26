package pinacolada.cards.pcl.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;

public class OrbCore_Earth extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Earth.class, PCLOrbHelper.Earth)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Earth()
    {
        super(DATA, 1, 6);

        SetAffinity_Orange(1);
    }
}
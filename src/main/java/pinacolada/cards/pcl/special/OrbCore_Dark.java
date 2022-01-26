package pinacolada.cards.pcl.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;

public class OrbCore_Dark extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Dark.class, PCLOrbHelper.Dark)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Dark()
    {
        super(DATA, 3, 4);

        SetAffinity_Dark(1);
    }
}
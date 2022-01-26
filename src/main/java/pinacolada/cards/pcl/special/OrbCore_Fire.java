package pinacolada.cards.pcl.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;

public class OrbCore_Fire extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Fire.class, PCLOrbHelper.Fire)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Fire()
    {
        super(DATA, 1, 4);

        SetAffinity_Red(1);
    }
}
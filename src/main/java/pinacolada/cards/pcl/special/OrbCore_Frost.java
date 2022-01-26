package pinacolada.cards.pcl.special;

import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.PCLOrbHelper;

public class OrbCore_Frost extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Frost.class, PCLOrbHelper.Frost)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Frost()
    {
        super(DATA, 1, 5);

        SetAffinity_Blue(1);
    }
}
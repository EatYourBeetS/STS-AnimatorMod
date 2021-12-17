package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Earth;
import pinacolada.resources.GR;

public class OrbCore_Earth extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Earth.class, GR.Tooltips.Earth, GR.Tooltips.Endurance, GR.Tooltips.Affinity_Orange)
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
package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Air;
import pinacolada.resources.GR;

public class OrbCore_Air extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Air.class, GR.Tooltips.Air, GR.Tooltips.Velocity, GR.Tooltips.Affinity_Green)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Air()
    {
        super(DATA, 1,6);

        SetAffinity_Green(1);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Air.class;
    }
}
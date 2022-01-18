package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Fire;
import pinacolada.resources.GR;

public class OrbCore_Fire extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Fire.class, GR.Tooltips.Fire, GR.Tooltips.Might, GR.Tooltips.Affinity_Red)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Fire()
    {
        super(DATA, 1, 4);

        SetAffinity_Red(1);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Fire.class;
    }
}
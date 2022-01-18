package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;

public class OrbCore_Lightning extends OrbCore
{
    public static final PCLCardData DATA = RegisterOrbCore(OrbCore_Lightning.class, GR.Tooltips.Lightning, GR.Tooltips.Invocation, GR.Tooltips.Affinity_Light)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Lightning()
    {
        super(DATA, 2, 4);

        SetAffinity_Light(1);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Lightning.class;
    }
}
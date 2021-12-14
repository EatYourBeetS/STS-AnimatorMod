package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.resources.GR;

public class OrbCore_Fire extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Fire.class, GR.Tooltips.Fire, GR.Tooltips.Might, GR.Tooltips.Affinity_Red)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Fire()
    {
        super(DATA, 1, 4);

        SetAffinity_Red(2);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Fire.class;
    }
}
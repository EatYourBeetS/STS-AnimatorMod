package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.resources.GR;

public class OrbCore_Water extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Water.class, GR.Tooltips.Water, GR.Tooltips.Wisdom, GR.Tooltips.Affinity_Blue)
            .SetPower(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Water()
    {
        super(DATA, 1, 9);

        SetAffinity_Blue(2);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Water.class;
    }
}
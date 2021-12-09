package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;

public class OrbCore_Frost extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Frost.class, GR.Tooltips.Frost, GR.Tooltips.Wisdom, GR.Tooltips.Affinity_Blue)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Frost()
    {
        super(DATA, 1, 5);

        SetAffinity_Blue(1);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Frost.class;
    }
}
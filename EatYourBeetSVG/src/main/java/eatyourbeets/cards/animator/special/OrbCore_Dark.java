package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;

public class OrbCore_Dark extends OrbCore
{
    public static final EYBCardData DATA = RegisterOrbCore(OrbCore_Dark.class, GR.Tooltips.Dark, GR.Tooltips.Corruption, GR.Tooltips.Affinity_Dark)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public OrbCore_Dark()
    {
        super(DATA, 3, 4);

        SetAffinity_Dark(2);
    }

    @Override
    public Class<? extends AbstractOrb> GetOrb() {
        return Dark.class;
    }
}
package eatyourbeets.cards.animator.ultrarare;

import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;

public class Cthulhu extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Cthulhu.class)
            .SetAttack(12, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CallOfCthulhu);

    public Cthulhu()
    {
        super(DATA);

        Initialize(800, 0, 120);

        SetAffinity_Dark(2, 0, 32);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }
}
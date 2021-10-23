package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;

public class RandomAttack extends AnimatorCard{
    public static final EYBCardData DATA = Register(RandomAttack.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);

    public RandomAttack() {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);
    }


}
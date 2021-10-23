package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class RandomSkill extends AnimatorCard {
    public static final EYBCardData DATA = Register(RandomSkill.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public RandomSkill() {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);
    }


}
package eatyourbeets.cards.animator.beta.colorless;

import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class MysteryCard extends AbstractMysteryCard
{
    public static final EYBCardData DATA = Register(MysteryCard.class)
            .SetImagePath(QuestionMark.DATA.ImagePath)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS);

    public MysteryCard() {
        this(false);
    }

    public MysteryCard(boolean isDummy)
    {
        super(DATA, isDummy, CardRarity.COMMON);
        Initialize(0, 0, 1, 0);
    }
}
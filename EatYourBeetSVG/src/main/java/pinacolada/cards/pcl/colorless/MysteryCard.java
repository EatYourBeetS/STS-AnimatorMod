package pinacolada.cards.pcl.colorless;

import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.colorless.uncommon.QuestionMark;

public class MysteryCard extends AbstractMysteryCard
{
    public static final PCLCardData DATA = Register(MysteryCard.class)
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
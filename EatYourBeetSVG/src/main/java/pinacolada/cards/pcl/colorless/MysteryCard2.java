package pinacolada.cards.pcl.colorless;

import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.PCLCardData;

public class MysteryCard2 extends AbstractMysteryCard
{
    public static final PCLCardData DATA = Register(MysteryCard2.class)
            .SetImagePath(QuestionMark.DATA.ImagePath)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS);

    public MysteryCard2() {
        this(false);
    }

    public MysteryCard2(boolean isDummy)
    {
        super(DATA, isDummy, CardRarity.COMMON);
        Initialize(0, 0, 2, 0);
    }

}
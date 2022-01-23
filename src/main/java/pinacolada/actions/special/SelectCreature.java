package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.cards.base.PCLCard;
import pinacolada.utilities.PCLJUtils;

public class SelectCreature extends eatyourbeets.actions.special.SelectCreature
{
    public SelectCreature(Targeting targeting, String sourceName)
    {
        super(targeting, sourceName);
    }

    public SelectCreature(AbstractCard card)
    {
        super(card);

        this.card = card;

        PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
        if (c != null && c.attackTarget != null)
        {
            switch (c.attackTarget)
            {
                case Self:
                    targeting = Targeting.Player;
                    break;
                case None:
                    targeting = Targeting.None;
                    break;
                case Normal:
                    targeting = Targeting.Enemy;
                    break;
                case ALL:
                    targeting = Targeting.AoE;
                    break;
                case Random:
                    targeting = Targeting.Random;
                    break;
            }
        }
        else
        {
            switch (card.target)
            {
                case ENEMY:
                case SELF_AND_ENEMY:
                    targeting = Targeting.Enemy;
                    break;
                case ALL:
                case ALL_ENEMY:
                    targeting = Targeting.AoE;
                    break;
                case SELF:
                    targeting = Targeting.Player;
                    break;
                case NONE:
                    targeting = Targeting.None;
                    break;
            }
        }

        Initialize(amount, card.name);
    }
}

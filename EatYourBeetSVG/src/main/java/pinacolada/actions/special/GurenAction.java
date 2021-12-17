package pinacolada.actions.special;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.actions.cardManipulation.PlayCard;
import pinacolada.cards.pcl.series.OwariNoSeraph.Guren;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GurenAction extends PlayCard
{
    public GurenAction(AbstractCreature target)
    {
        super(null, target, false, true);

        Initialize(player, target, 1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.drawPile.isEmpty())
        {
            if (player.discardPile.size() > 0)
            {
                PCLActions.Top.Add(new GurenAction(target));
                PCLActions.Top.Add(new EmptyDeckShuffleAction());
            }

            Complete();
            return;
        }

        sourcePile = player.drawPile;
        card = player.drawPile.getTopCard();
        super.FirstUpdate();
    }

    @Override
    protected boolean CanUse()
    {
        boolean canUse = super.CanUse();

        if (card.type == AbstractCard.CardType.ATTACK)
        {
            SetExhaust(true);
        }
        else if (PCLGameUtilities.IsHindrance(card))
        {
            SetExhaust(true);
            canUse = false;
        }
        else if (card instanceof Guren && !((Guren)card).CanAutoPlay(this))
        {
            SetExhaust(false);
            canUse = false;
        }

        return canUse;
    }
}

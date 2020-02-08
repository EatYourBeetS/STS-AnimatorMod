package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.cardManipulation.PlayCard;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Guren;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class GurenAction extends PlayCard
{
    public GurenAction(AbstractCreature target)
    {
        super(null, target, false);

        Initialize(player, target, 1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.drawPile.isEmpty())
        {
            if (player.discardPile.size() > 0)
            {
                GameActions.Top.Add(new GurenAction(this.target));
                GameActions.Top.Add(new EmptyDeckShuffleAction());
            }

            Complete();
            return;
        }

        card = player.drawPile.getTopCard();
        SetSourcePile(player.drawPile);
        sourcePile.removeCard(card);

        if (targetPosition == null)
        {
            SetTargetPosition(DEFAULT_TARGET_X_RIGHT, DEFAULT_TARGET_Y);
        }

        ShowCard();
    }

    @Override
    protected boolean CanUse()
    {
        boolean canUse = super.CanUse();

        if (card.type == AbstractCard.CardType.ATTACK)
        {
            SetExhaust(true);
        }
        else if (GameUtilities.IsCurseOrStatus(card))
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

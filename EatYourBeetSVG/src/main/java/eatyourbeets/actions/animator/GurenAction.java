package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.cardManipulation.PlayCard;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

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
                GameActions.Top.Add(new GurenAction(target));
                GameActions.Top.Add(new EmptyDeckShuffleAction());
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

        if (!GameUtilities.HasAffinity(card, Affinity.Light) && !GameUtilities.HasAffinity(card, Affinity.Dark))
        {
            SetExhaust(true);
        }

        if (GameUtilities.IsHindrance(card))
        {
            canUse = false;
        }

        return canUse;
    }
}

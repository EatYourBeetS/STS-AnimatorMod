package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.cardManipulation.PlayCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class GurenAction extends PlayCard
{
    public GurenAction(AbstractCreature target)
    {
        super(null, target, false, true);

        SetExhaust(true);

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
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        if (isDone && card != null && GameUtilities.IsHindrance(card))
        {
            boolean added = false;
            final ArrayList<AbstractGameAction> actions = GameActions.GetActions();
            for (int i = 0; i < actions.size(); i++)
            {
                if (actions.get(i) instanceof GurenAction)
                {
                    actions.add(i, new GurenAction(target));
                    added = true;
                    break;
                }
            }

            if (!added)
            {
                GameActions.Top.Add(new GurenAction(target));
            }
        }
    }

    @Override
    protected boolean CanUse()
    {
        return !GameUtilities.IsHindrance(card) && super.CanUse();
    }
}

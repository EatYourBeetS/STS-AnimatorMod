package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;

public class ReshuffleDiscardPile extends EYBAction
{
    protected boolean onlyIfEmpty;

    public ReshuffleDiscardPile(boolean onlyIfEmpty)
    {
        super(ActionType.SHUFFLE);

        this.onlyIfEmpty = onlyIfEmpty;

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!onlyIfEmpty || player.drawPile.isEmpty())
        {
            GameActions.Top.Add(new EmptyDeckShuffleAction());
        }

        Complete();
    }
}

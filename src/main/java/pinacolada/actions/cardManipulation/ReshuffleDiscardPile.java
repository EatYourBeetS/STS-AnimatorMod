package pinacolada.actions.cardManipulation;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import eatyourbeets.actions.EYBAction;
import pinacolada.utilities.PCLActions;

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
            PCLActions.Top.Add(new EmptyDeckShuffleAction());
        }

        Complete();
    }
}

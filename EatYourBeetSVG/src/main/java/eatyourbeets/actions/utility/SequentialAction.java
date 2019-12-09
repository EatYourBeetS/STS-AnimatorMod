package eatyourbeets.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActions;

public class SequentialAction extends EYBAction
{
    private final AbstractGameAction action;
    private final AbstractGameAction action2;

    public SequentialAction(AbstractGameAction action, AbstractGameAction action2)
    {
        super(action.actionType);

        this.action = action;
        this.action2 = action2;

        Initialize(action.source, action.target, action.amount, "");
    }

    @Override
    public void update()
    {
        if (updateAction())
        {
            GameActions.Top.Add(action2);

            this.isDone = true;
        }
    }

    private boolean updateAction()
    {
        if (!action.isDone)
        {
            action.update();
        }

        return action.isDone;
    }
}

package eatyourbeets.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;

public class CallbackAction extends EYBActionWithCallback<AbstractGameAction>
{
    private final AbstractGameAction action;

    public CallbackAction(AbstractGameAction action, ActionT1<AbstractGameAction> onCompletion)
    {
        this(action);

        AddCallback(onCompletion);
    }

    public CallbackAction(AbstractGameAction action, Object state, ActionT2<Object, AbstractGameAction> onCompletion)
    {
        this(action);

        AddCallback(state, onCompletion);
    }

    public CallbackAction(AbstractGameAction action)
    {
        super(action.actionType);

        this.action = action;

        Initialize(action.source, action.target, action.amount);
    }

    @Override
    public void update()
    {
        if (updateAction())
        {
            Complete(action);
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

package eatyourbeets.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.actions.EYBActionWithCallback;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CallbackAction extends EYBActionWithCallback<AbstractGameAction>
{
    private final AbstractGameAction action;

    public CallbackAction(AbstractGameAction action, Consumer<AbstractGameAction> onCompletion)
    {
        this(action);

        AddCallback(onCompletion);
    }

    public CallbackAction(AbstractGameAction action, Object state, BiConsumer<Object, AbstractGameAction> onCompletion)
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

package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.GenericCallback;

import java.util.ArrayList;

public abstract class EYBActionWithCallback<T> extends EYBAction
{
    protected ArrayList<GenericCallback<T>> callbacks = new ArrayList<>();

    public EYBActionWithCallback(AbstractGameAction.ActionType type)
    {
        super(type);
    }

    public EYBActionWithCallback(AbstractGameAction.ActionType type, float duration)
    {
        super(type, duration);
    }

    public <S> EYBActionWithCallback<T> AddCallback(S state, ActionT2<S, T> onCompletion)
    {
        callbacks.add(GenericCallback.FromT2(onCompletion, state));

        return this;
    }

    public EYBActionWithCallback<T> AddCallback(ActionT1<T> onCompletion)
    {
        callbacks.add(GenericCallback.FromT1(onCompletion));

        return this;
    }

    public EYBActionWithCallback<T> AddCallback(ActionT0 onCompletion)
    {
        callbacks.add(GenericCallback.FromT0(onCompletion));

        return this;
    }

    protected void Complete(T result)
    {
        for (GenericCallback<T> callback : callbacks)
        {
            callback.Complete(result);
        }

        Complete();
    }

    @Override
    protected void Import(EYBAction other)
    {
        super.Import(other);

        callbacks.addAll(((EYBActionWithCallback<T>)other).callbacks);
    }
}

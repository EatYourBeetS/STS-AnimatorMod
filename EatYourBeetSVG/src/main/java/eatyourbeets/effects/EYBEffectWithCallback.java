package eatyourbeets.effects;

import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.GenericCallback;

import java.util.ArrayList;

public abstract class EYBEffectWithCallback<T> extends EYBEffect
{
    protected ArrayList<GenericCallback<T>> callbacks = new ArrayList<>();

    public EYBEffectWithCallback()
    {
        super();
    }

    public EYBEffectWithCallback(int amount)
    {
        super();
    }

    public EYBEffectWithCallback(int amount, float duration)
    {
        super(duration);
    }

    public <S> EYBEffectWithCallback<T> AddCallback(S state, ActionT2<S, T> onCompletion)
    {
        callbacks.add(GenericCallback.FromT2(onCompletion, state));

        return this;
    }

    public EYBEffectWithCallback<T> AddCallback(ActionT1<T> onCompletion)
    {
        callbacks.add(GenericCallback.FromT1(onCompletion));

        return this;
    }

    public EYBEffectWithCallback<T> AddCallback(ActionT0 onCompletion)
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
}

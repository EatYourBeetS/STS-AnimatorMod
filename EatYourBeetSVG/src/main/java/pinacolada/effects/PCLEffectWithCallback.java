package pinacolada.effects;

import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.utilities.GenericCallback;

import java.util.ArrayList;

public abstract class PCLEffectWithCallback<T> extends PCLEffect
{
    protected ArrayList<GenericCallback<T>> callbacks = new ArrayList<>();

    public PCLEffectWithCallback()
    {
        super();
    }

    public PCLEffectWithCallback(float duration)
    {
        super(duration);
    }

    public PCLEffectWithCallback(float duration, boolean isRealtime)
    {
        super(duration, isRealtime);
    }

    public <S> PCLEffectWithCallback<T> AddCallback(S state, ActionT2<S, T> onCompletion)
    {
        callbacks.add(GenericCallback.FromT2(onCompletion, state));

        return this;
    }

    public PCLEffectWithCallback<T> AddCallback(ActionT1<T> onCompletion)
    {
        callbacks.add(GenericCallback.FromT1(onCompletion));

        return this;
    }

    public PCLEffectWithCallback<T> AddCallback(ActionT0 onCompletion)
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

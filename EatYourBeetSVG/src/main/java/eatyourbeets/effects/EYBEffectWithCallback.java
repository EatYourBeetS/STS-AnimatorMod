package eatyourbeets.effects;

import eatyourbeets.utilities.GenericCallback;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class EYBEffectWithCallback<T> extends EYBEffect
{
    protected ArrayList<GenericCallback<T>> callbacks = new ArrayList<>();

    public EYBEffectWithCallback(int amount)
    {
        super(0);
    }

    public EYBEffectWithCallback(int amount, float duration)
    {
        super(amount, duration);
    }

    public EYBEffectWithCallback<T> AddCallback(Object state, BiConsumer<Object, T> onCompletion)
    {
        callbacks.add(new GenericCallback<>(state, onCompletion));

        return this;
    }

    public EYBEffectWithCallback<T> AddCallback(Consumer<T> onCompletion)
    {
        callbacks.add(new GenericCallback<>(onCompletion));

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

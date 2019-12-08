package eatyourbeets.actions;

import eatyourbeets.utilities.GenericCallback;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class EYBActionWithCallback<T> extends EYBAction
{
    protected ArrayList<GenericCallback<T>> callbacks = new ArrayList<>();

    public EYBActionWithCallback(ActionType type)
    {
        super(type);
    }

    public EYBActionWithCallback(ActionType type, float duration)
    {
        super(type, duration);
    }

    public EYBActionWithCallback<T> AddCallback(Object state, BiConsumer<Object, T> onCompletion)
    {
        callbacks.add(new GenericCallback<>(state, onCompletion));

        return this;
    }

    public EYBActionWithCallback<T> AddCallback(Consumer<T> onCompletion)
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

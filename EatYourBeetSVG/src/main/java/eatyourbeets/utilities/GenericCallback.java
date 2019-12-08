package eatyourbeets.utilities;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GenericCallback<T>
{
    protected Object state;
    protected BiConsumer<Object, T> biConsumer;
    protected Consumer<T> consumer;

    public GenericCallback(Object state, BiConsumer<Object, T> onCompletion)
    {
        this.state = state;
        this.biConsumer = onCompletion;
    }

    public GenericCallback(Consumer<T> onCompletion)
    {
        this.consumer = onCompletion;
    }

    public void Complete(T result)
    {
        if (biConsumer != null)
        {
            biConsumer.accept(state, result);
        }

        if (consumer != null)
        {
            consumer.accept(result);
        }
    }
}

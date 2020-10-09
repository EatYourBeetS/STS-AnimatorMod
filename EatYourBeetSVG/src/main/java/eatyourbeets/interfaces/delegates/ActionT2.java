package eatyourbeets.interfaces.delegates;

public interface ActionT2<T1, T2>
{
    void Invoke(T1 arg1, T2 arg2);

    default void CastAndInvoke(Object arg1, T2 arg2)
    {
        Invoke((T1)arg1, arg2);
    }
}

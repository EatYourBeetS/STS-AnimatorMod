package eatyourbeets.interfaces.delegates;

public interface ActionT1<T1>
{
    void Invoke(T1 arg1);

    default void CastAndInvoke(Object arg1)
    {
        Invoke((T1)arg1);
    }
}

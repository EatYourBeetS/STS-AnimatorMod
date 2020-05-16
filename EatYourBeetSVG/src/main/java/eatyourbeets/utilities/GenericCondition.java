package eatyourbeets.utilities;

import eatyourbeets.interfaces.delegates.*;

public class GenericCondition<T>
{
    protected Object state;
    protected FuncT0<Boolean> conditionT0;
    protected FuncT1<Boolean, T> conditionT1;
    protected FuncT2<Boolean, Object, T> conditionT2;

    public static <T> GenericCondition<T> FromT0(FuncT0<Boolean> condition)
    {
        return new GenericCondition<>(condition);
    }

    public static <T> GenericCondition<T> FromT1(FuncT1<Boolean, T> condition)
    {
        return new GenericCondition<>(condition);
    }

    public static <T> GenericCondition<T> FromT2(FuncT2<Boolean, Object, T> condition, Object state)
    {
        return new GenericCondition<>(condition, state);
    }

    private GenericCondition(FuncT2<Boolean, Object, T> condition, Object state)
    {
        this.state = state;
        this.conditionT2 = condition;
    }

    private GenericCondition(FuncT1<Boolean, T> condition)
    {
        this.conditionT1 = condition;
    }

    private GenericCondition(FuncT0<Boolean> condition)
    {
        this.conditionT0 = condition;
    }

    public boolean Check(T result)
    {
        if (conditionT2 != null)
        {
            return conditionT2.Invoke(state, result);
        }
        if (conditionT1 != null)
        {
            return conditionT1.Invoke(result);
        }
        if (conditionT0 != null)
        {
            return conditionT0.Invoke();
        }

        JavaUtilities.GetLogger(this).warn("No Condition found: " + getClass().getName());
        return true;
    }
}

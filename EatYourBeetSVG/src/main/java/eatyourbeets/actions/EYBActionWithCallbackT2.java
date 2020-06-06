package eatyourbeets.actions;

import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.GenericCondition;

import java.util.ArrayList;

public abstract class EYBActionWithCallbackT2<T, C> extends EYBActionWithCallback<T>
{
    protected ArrayList<GenericCondition<C>> conditions = new ArrayList<>();

    public EYBActionWithCallbackT2(ActionType type)
    {
        super(type);
    }

    public EYBActionWithCallbackT2(ActionType type, float duration)
    {
        super(type, duration);
    }

    public <S> EYBActionWithCallbackT2<T, C> AddCondition(S state, FuncT2<Boolean, S, C> condition)
    {
        conditions.add(GenericCondition.FromT2(condition, state));

        return this;
    }

    public EYBActionWithCallbackT2<T, C> AddCondition(FuncT1<Boolean, C> condition)
    {
        conditions.add(GenericCondition.FromT1(condition));

        return this;
    }

    public EYBActionWithCallbackT2<T, C> AddCondition(FuncT0<Boolean> condition)
    {
        conditions.add(GenericCondition.FromT0(condition));

        return this;
    }

    protected boolean CheckConditions(C result)
    {
        for (GenericCondition<C> callback : conditions)
        {
            if (!callback.Check(result))
            {
                return false;
            }
        }

        return true;
    }
}

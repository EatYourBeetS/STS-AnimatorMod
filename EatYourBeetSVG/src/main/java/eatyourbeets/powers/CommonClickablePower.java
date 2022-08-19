package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;

public abstract class CommonClickablePower extends EYBClickablePower
{
    public static String CreateFullID(Class<? extends CommonClickablePower> type)
    {
        return GR.Common.CreateID(type.getSimpleName());
    }

    public CommonClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, id, type, requiredAmount);
    }

    public CommonClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, id, type, requiredAmount, checkCondition, payCost);
    }
}

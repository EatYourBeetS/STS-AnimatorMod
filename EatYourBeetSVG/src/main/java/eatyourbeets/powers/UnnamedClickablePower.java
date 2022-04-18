package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;

public abstract class UnnamedClickablePower extends EYBClickablePower
{
    public static String CreateFullID(Class<? extends UnnamedClickablePower> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public UnnamedClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, id, type, requiredAmount);
    }

    public UnnamedClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, id, type, requiredAmount, checkCondition, payCost);
    }

    public UnnamedClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, cardData, type, requiredAmount);
    }

    public UnnamedClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, cardData, type, requiredAmount, checkCondition, payCost);
    }

    public UnnamedClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, relic, type, requiredAmount);
    }

    public UnnamedClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, relic, type, requiredAmount, checkCondition, payCost);
    }
}

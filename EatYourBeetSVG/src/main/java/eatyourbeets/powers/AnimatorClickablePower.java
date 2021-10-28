package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.relics.EYBRelic;

public abstract class AnimatorClickablePower extends EYBClickablePower
{
    public AnimatorClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, cardData, type, requiredAmount);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, cardData, type, requiredAmount, checkCondition, payCost);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost, Affinity... affinities)
    {
        super(owner, cardData, type, requiredAmount, checkCondition, payCost, affinities);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, relic, type, requiredAmount);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, relic, type, requiredAmount, checkCondition, payCost);
    }

    public AnimatorClickablePower(AbstractCreature owner, String orignialID, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, orignialID, type, requiredAmount);
    }

    public AnimatorClickablePower(AbstractCreature owner, String orignialID, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, orignialID, type, requiredAmount, checkCondition, payCost);
    }
}

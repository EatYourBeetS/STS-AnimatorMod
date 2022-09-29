package eatyourbeets.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;

public abstract class AnimatorClickablePower extends EYBClickablePower
{
    public static String CreateFullID(Class<? extends AnimatorClickablePower> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.Animator.PlayerClass;
    }

    public AnimatorClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, id, type, requiredAmount);
    }

    public AnimatorClickablePower(AbstractCreature owner, String id, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, id, type, requiredAmount, checkCondition, payCost);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, cardData, type, requiredAmount);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBCardData cardData, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, cardData, type, requiredAmount, checkCondition, payCost);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount)
    {
        super(owner, relic, type, requiredAmount);
    }

    public AnimatorClickablePower(AbstractCreature owner, EYBRelic relic, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        super(owner, relic, type, requiredAmount, checkCondition, payCost);
    }
}

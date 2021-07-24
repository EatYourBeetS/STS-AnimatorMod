package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PowerTriggerCondition
{
    public final EYBPower power;
    public final PowerTriggerConditionType type;
    public boolean refreshEachTurn;
    public boolean usePowerAmount;
    public int requiredAmount;
    public int baseUses;
    public int uses;

    private static final ActionT1<Integer> EMPTY_ACTION = __ -> {};
    private static final FuncT1<Boolean, Integer> EMPTY_FUNCTION = __ -> true;
    private final FuncT1<Boolean, Integer> checkCondition;
    private final ActionT1<Integer> payCost;
    private boolean canUse;

    protected PowerTriggerCondition(EYBPower power, PowerTriggerConditionType type, int requiredAmount)
    {
        this(power, type, requiredAmount, null, null);
    }

    protected PowerTriggerCondition(EYBPower power, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(power, PowerTriggerConditionType.Special, requiredAmount, checkCondition, payCost);
    }

    private PowerTriggerCondition(EYBPower power, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this.power = power;
        this.type = type;
        this.requiredAmount = requiredAmount;
        this.checkCondition = checkCondition != null ? checkCondition : EMPTY_FUNCTION;
        this.payCost = payCost != null ? payCost : EMPTY_ACTION;
        this.baseUses = this.uses = 1;
        this.refreshEachTurn = true;
        this.usePowerAmount = false;
    }

    public PowerTriggerCondition AddUses(int uses)
    {
        this.baseUses += uses;
        this.uses += uses;

        return this;
    }

    public PowerTriggerCondition SetUses(int uses, boolean refreshEachTurn, boolean usePowerAmount)
    {
        this.baseUses = this.uses = uses;
        this.refreshEachTurn = refreshEachTurn;
        this.usePowerAmount = usePowerAmount;

        return this;
    }

    public PowerTriggerCondition SetUsesFromPowerAmount(boolean refreshEachTurn)
    {
        return SetUses(power.amount, refreshEachTurn, true);
    }

    public void Refresh()
    {
        canUse = uses > 0 && CheckCondition();
    }

    public boolean CanUse()
    {
        return canUse;
    }

    public boolean CheckCondition()
    {
        switch (type)
        {
            case Energy: return AbstractDungeon.player.energy.energy >= requiredAmount;

            case Discard: case Exhaust: return AbstractDungeon.player.hand.size() >= requiredAmount;

            case LoseHP: return GameUtilities.GetActualHealth(AbstractDungeon.player) > requiredAmount;

            case Special: return checkCondition.Invoke(requiredAmount);

            case None: default: return true;
        }
    }

    public void Use()
    {
        if (!canUse)
        {
            return;
        }

        switch (type)
        {
            case Energy:
            {
                GameActions.Bottom.SpendEnergy(requiredAmount, false);
                break;
            }
            case Discard:
            {
                GameActions.Bottom.DiscardFromHand(power.name, requiredAmount, false).SetOptions(false, false, false);
                break;
            }
            case Exhaust:
            {
                GameActions.Bottom.ExhaustFromHand(power.name, requiredAmount, false).SetOptions(false, false, false);
                break;
            }
            case LoseHP:
            {
                GameActions.Bottom.LoseHP(requiredAmount, AbstractGameAction.AttackEffect.NONE);
                break;
            }
            case Special:
            {
                payCost.Invoke(requiredAmount);
                break;
            }
        }

        uses -= 1;
        power.onSpecificTrigger();
        power.flashWithoutSound();
        power.updateDescription();
    }
}
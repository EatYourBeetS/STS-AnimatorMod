package eatyourbeets.powers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PowerTriggerCondition
{
    public final EYBClickablePower power;
    public final PowerTriggerConditionType type;
    public boolean refreshEachTurn;
    public boolean stackAutomatically;
    public boolean requiresTarget;
    public int requiredAmount;
    public int baseUses;
    public int uses;

    private static final ActionT1<Integer> EMPTY_ACTION = __ -> {};
    private static final FuncT1<Boolean, Integer> EMPTY_FUNCTION = __ -> true;
    private FuncT1<Boolean, Integer> checkCondition;
    private ActionT1<Integer> payCost;
    private boolean canUse;

    protected PowerTriggerCondition(EYBClickablePower power, PowerTriggerConditionType type, int requiredAmount)
    {
        this(power, type, requiredAmount, null, null);
    }

    protected PowerTriggerCondition(EYBClickablePower power, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(power, PowerTriggerConditionType.Special, requiredAmount, checkCondition, payCost);
    }

    private PowerTriggerCondition(EYBClickablePower power, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this.power = power;
        this.type = type;
        this.requiredAmount = requiredAmount;
        this.baseUses = this.uses = 1;
        this.refreshEachTurn = true;
        this.stackAutomatically = false;

        SetCheckCondition(checkCondition)
        .SetPayCost(payCost)
        .Refresh(false);
    }

    public PowerTriggerCondition SetCheckCondition(FuncT1<Boolean, Integer> checkCondition)
    {
        this.checkCondition = checkCondition == null ? EMPTY_FUNCTION : checkCondition;

        return this;
    }

    public PowerTriggerCondition SetPayCost(ActionT1<Integer> payCost)
    {
        this.payCost = payCost == null ? EMPTY_ACTION : payCost;

        return this;
    }

    public PowerTriggerCondition AddUses(int uses)
    {
        if (HasInfiniteUses())
        {
            throw new RuntimeException("Tried to add more uses to a condition with infinite uses.");
        }

        this.baseUses += uses;
        this.uses += uses;
        this.power.updateDescription();
        Refresh(false);

        return this;
    }

    public PowerTriggerCondition SetUses(int uses, boolean refreshEachTurn, boolean stackAutomatically)
    {
        this.baseUses = this.uses = uses;
        this.refreshEachTurn = refreshEachTurn;
        this.stackAutomatically = stackAutomatically;
        this.power.updateDescription();
        Refresh(false);

        return this;
    }

    public PowerTriggerCondition SetOneUsePerPower(boolean refreshEachTurn)
    {
        return SetUses(1, refreshEachTurn, true);
    }

    public void Refresh(boolean startOfTurn)
    {
        if (startOfTurn && refreshEachTurn)
        {
            uses = baseUses;
            power.updateDescription();
        }

        canUse = (HasInfiniteUses() || uses > 0) && CheckCondition();
    }

    public boolean HasInfiniteUses()
    {
        return baseUses == -1;
    }

    public boolean CanUse()
    {
        return canUse;
    }

    public boolean CheckCondition()
    {
        final boolean result = checkCondition == null || checkCondition.Invoke(requiredAmount);
        switch (type)
        {
            case Energy:
                return result && EnergyPanel.getCurrentEnergy() >= requiredAmount;

            case Discard:
            case Exhaust:
                return result && AbstractDungeon.player.hand.size() >= requiredAmount;

            case TakeDamage:
            case TakeDelayedDamage:
                return result && GameUtilities.GetHP(AbstractDungeon.player, true, true) > requiredAmount;

            case LoseHP:
                return result && GameUtilities.GetHP(AbstractDungeon.player, true, false) > requiredAmount;

            case Gold:
                return result && AbstractDungeon.player.gold >= requiredAmount;

            default:
                return result;
        }
    }

    public void Use(AbstractMonster m)
    {
        if (!canUse)
        {
            return;
        }

        if (payCost != null)
        {
            payCost.Invoke(requiredAmount);
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
            case TakeDamage:
            {
                GameActions.Bottom.SFX(SFX.BLOOD_SPLAT, 1.2f, 1.3f);
                GameActions.Bottom.TakeDamage(requiredAmount, AttackEffects.NONE);
                break;
            }
            case TakeDelayedDamage:
            {
                GameActions.Bottom.DealDamageAtEndOfTurn(power.owner, power.owner, requiredAmount);
                break;
            }
            case LoseHP:
            {
                GameActions.Bottom.SFX(SFX.BLOOD_SPLAT, 1.2f, 1.3f);
                GameActions.Bottom.LoseHP(requiredAmount, AttackEffects.NONE);
                break;
            }
            case Gold:
            {
                AbstractDungeon.player.loseGold(requiredAmount);
                SFX.Play(SFX.EVENT_PURCHASE, 0.95f, 1.05f);
                break;
            }
        }

        if (!HasInfiniteUses())
        {
            uses -= 1;
        }

        power.OnUse(m);
        power.flashWithoutSound();
        Refresh(false);
        power.updateDescription();

        CombatStats.OnClickablePowerUsed(power, m);
    }
}
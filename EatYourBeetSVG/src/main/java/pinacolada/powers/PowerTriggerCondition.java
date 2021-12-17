package pinacolada.powers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class PowerTriggerCondition
{
    public final PCLClickablePower power;
    public final PowerTriggerConditionType type;
    public boolean refreshEachTurn;
    public boolean stackAutomatically;
    public boolean requiresTarget;
    public int requiredAmount;
    public int baseUses;
    public int uses;
    public PCLAffinity[] affinities;

    private static final ActionT1<Integer> EMPTY_ACTION = __ -> {};
    private static final FuncT1<Boolean, Integer> EMPTY_FUNCTION = __ -> true;
    public FuncT1<Boolean, Integer> checkCondition;
    public ActionT1<Integer> payCost;
    private boolean canUse;

    protected PowerTriggerCondition(PCLClickablePower power, PowerTriggerConditionType type, int requiredAmount)
    {
        this(power, type, requiredAmount, null, null);
    }

    protected PowerTriggerCondition(PCLClickablePower power, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(power, PowerTriggerConditionType.Special, requiredAmount, checkCondition, payCost);
    }

    protected PowerTriggerCondition(PCLClickablePower power, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost)
    {
        this(power, type, requiredAmount, checkCondition, payCost, PCLAffinity.Extended());
    }

    protected PowerTriggerCondition(PCLClickablePower power, PowerTriggerConditionType type, int requiredAmount, FuncT1<Boolean, Integer> checkCondition, ActionT1<Integer> payCost, PCLAffinity... affinities)
    {
        this.power = power;
        this.type = type;
        this.requiredAmount = requiredAmount;
        this.baseUses = this.uses = 1;
        this.refreshEachTurn = true;
        this.stackAutomatically = false;
        this.affinities = affinities;

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
                return result && PCLGameUtilities.GetHP(AbstractDungeon.player, true, true) > requiredAmount;

            case Affinity:
                return result && PCLCombatStats.MatchingSystem.CheckAffinityLevels(affinities, requiredAmount,true);

            case LoseHP:
                return result && PCLGameUtilities.GetHP(AbstractDungeon.player, true, false) > requiredAmount;

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

        boolean shouldPayCost = PCLCombatStats.OnClickablePowerUsed(power, m);

        if (shouldPayCost) {
            if (payCost != null)
            {
                payCost.Invoke(requiredAmount);
            }

            switch (type)
            {
                case Energy:
                {
                    PCLActions.Bottom.SpendEnergy(requiredAmount, false);
                    break;
                }
                case Discard:
                {
                    PCLActions.Bottom.DiscardFromHand(power.name, requiredAmount, false).SetOptions(false, false, false);
                    break;
                }
                case Exhaust:
                {
                    PCLActions.Bottom.ExhaustFromHand(power.name, requiredAmount, false).SetOptions(false, false, false);
                    break;
                }
                case TakeDamage:
                {
                    PCLActions.Bottom.SFX(SFX.BLOOD_SPLAT, 1.2f, 1.3f);
                    PCLActions.Bottom.TakeDamage(requiredAmount, AttackEffects.NONE);
                    break;
                }
                case TakeDelayedDamage:
                {
                    PCLActions.Bottom.DealDamageAtEndOfTurn(power.owner, power.owner, requiredAmount);
                    break;
                }
                case Affinity:
                {
                    PCLActions.Bottom.TryChooseSpendAffinity(power.name, requiredAmount, affinities);
                    break;
                }
                case LoseHP:
                {
                    PCLActions.Bottom.SFX(SFX.BLOOD_SPLAT, 1.2f, 1.3f);
                    PCLActions.Bottom.LoseHP(requiredAmount, AttackEffects.NONE);
                    break;
                }
                case Gold:
                {
                    AbstractDungeon.player.loseGold(requiredAmount);
                    SFX.Play(SFX.EVENT_PURCHASE, 0.95f, 1.05f);
                    break;
                }
            }
        }

        if (!HasInfiniteUses())
        {
            uses -= 1;
        }

        power.OnUse(m, requiredAmount);
        power.flashWithoutSound();
        Refresh(false);
        power.updateDescription();
    }
}
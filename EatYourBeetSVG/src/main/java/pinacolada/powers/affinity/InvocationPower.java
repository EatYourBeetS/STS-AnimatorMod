package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import pinacolada.actions.powers.ApplyPower;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.interfaces.subscribers.OnGainTempHPSubscriber;
import pinacolada.interfaces.subscribers.OnGainTriggerablePowerBonusSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class InvocationPower extends AbstractPCLAffinityPower implements OnApplyPowerSubscriber, OnGainTempHPSubscriber, OnGainTriggerablePowerBonusSubscriber
{
    public static final String POWER_ID = CreateFullID(InvocationPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Light;

    public InvocationPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);
        PCLCombatStats.onApplyPower.Subscribe(this);
        PCLCombatStats.onGainTempHP.Subscribe(this);
        PCLCombatStats.onGainTriggerablePowerBonus.Subscribe(this);
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!isActive) {
            PCLCombatStats.onApplyPower.Unsubscribe(this);
            PCLCombatStats.onGainTempHP.Unsubscribe(this);
            PCLCombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
            return;
        }
        int applyAmount = (int) (power.amount * (GetEffectiveMultiplier() - 1));

        if (PCLGameUtilities.IsPlayer(power.owner) && PCLGameUtilities.IsCommonBuff(power)) {
            PCLActions.Last.Callback(() -> {
                AbstractPower po = PCLGameUtilities.GetPower(power.owner, power.ID);
                if (po != null) {
                    po.stackPower(applyAmount);
                    po.flash();
                    po.updateDescription();
                }
            });

            final AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
            if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
            {
                action.amount *= GetEffectiveMultiplier();
            }

            isActive = false;
            flash();
        }
    }

    @Override
    public int OnGainTempHP(int amount) {
        PCLCombatStats.onApplyPower.Unsubscribe(this);
        PCLCombatStats.onGainTempHP.Unsubscribe(this);
        PCLCombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
        if (!isActive) {
            return amount;
        }
        isActive = false;
        flash();
        return amount *= GetEffectiveMultiplier();
    }

    @Override
    public int OnGainTriggerablePowerBonus(String powerID, PCLTriggerablePower.Type gainType, int amount) {
        PCLCombatStats.onApplyPower.Unsubscribe(this);
        PCLCombatStats.onGainTempHP.Unsubscribe(this);
        PCLCombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
        if (!isActive) {
            return amount;
        }
        isActive = false;
        flash();
        return amount *= GetEffectiveMultiplier();
    }
}
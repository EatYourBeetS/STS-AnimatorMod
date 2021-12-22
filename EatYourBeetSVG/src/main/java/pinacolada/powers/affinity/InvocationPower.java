package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.interfaces.subscribers.OnGainTempHPSubscriber;
import pinacolada.interfaces.subscribers.OnGainTriggerablePowerBonusSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.utilities.PCLGameUtilities;

public class InvocationPower extends AbstractPCLAffinityPower implements OnTryApplyPowerListener, OnGainTempHPSubscriber, OnGainTriggerablePowerBonusSubscriber
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
        PCLCombatStats.onGainTempHP.Subscribe(this);
        PCLCombatStats.onGainTriggerablePowerBonus.Subscribe(this);
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        if (!isActive) {
            PCLCombatStats.onGainTempHP.Unsubscribe(this);
            PCLCombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
            return true;
        }
        if (PCLGameUtilities.IsPlayer(power.owner) && PCLGameUtilities.IsCommonBuff(power)) {
            power.amount = (int) (power.amount * GetEffectiveMultiplier());
            isActive = false;
            PCLCombatStats.onGainTempHP.Unsubscribe(this);
            PCLCombatStats.onGainTriggerablePowerBonus.Unsubscribe(this);
            flash();
        }
        return true;
    }

    @Override
    public int OnGainTempHP(int amount) {
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
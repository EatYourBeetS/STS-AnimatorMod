package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.interfaces.subscribers.OnPCLClickablePowerUsed;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;

public class InspirationPower extends PCLPower implements OnPCLClickablePowerUsed
{
    public static final String POWER_ID = CreateFullID(InspirationPower.class);

    public InspirationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onPCLClickablePowerUsed.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onPCLClickablePowerUsed.Unsubscribe(this);
    }

    @Override
    public boolean OnClickablePowerUsed(PCLClickablePower power, AbstractMonster target)
    {
        if (!power.triggerCondition.type.equals(PowerTriggerConditionType.None) && !(power instanceof AbstractPCLAffinityPower)) {
            ReducePower(1);

            this.flashWithoutSound();
        }
        return false;
    }
}

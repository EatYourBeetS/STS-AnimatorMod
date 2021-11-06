package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnClickablePowerUsed;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;

public class InspirationPower extends CommonPower implements OnClickablePowerUsed
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

        CombatStats.onClickablePowerUsed.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onClickablePowerUsed.Unsubscribe(this);
    }

    @Override
    public boolean OnClickablePowerUsed(EYBClickablePower power, AbstractMonster target)
    {
        if (!power.triggerCondition.type.equals(PowerTriggerConditionType.None)) {
            ReducePower(1);

            this.flashWithoutSound();
        }
        return false;
    }
}

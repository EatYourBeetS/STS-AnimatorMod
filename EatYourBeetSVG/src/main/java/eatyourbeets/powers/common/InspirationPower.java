package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnClickablePowerUsedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.utilities.GameActions;

public class InspirationPower extends CommonPower implements OnClickablePowerUsedSubscriber
{
    public static final String POWER_ID = CreateFullID(InspirationPower.class);

    public InspirationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.canBeZero = true;

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
    public void OnClickablePowerUsed(EYBClickablePower power, AbstractMonster target)
    {
        if (enabled & (amount > 0))
        {
            SetEnabled(false);
            ReducePower(1);
            GameActions.Bottom.Motivate(1);
            this.flashWithoutSound();
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        SetEnabled(true);
    }
}

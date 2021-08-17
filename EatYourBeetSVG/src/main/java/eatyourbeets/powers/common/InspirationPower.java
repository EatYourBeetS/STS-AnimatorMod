package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnClickablePowerUsed;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.utilities.GameActions;

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
    public void OnClickablePowerUsed(EYBClickablePower power, AbstractMonster target)
    {
        ReducePower(1);
        GameActions.Bottom.GainEnergy(1);

        this.flashWithoutSound();
    }
}

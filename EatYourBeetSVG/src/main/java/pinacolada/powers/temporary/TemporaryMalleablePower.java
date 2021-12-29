package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import eatyourbeets.interfaces.delegates.FuncT2;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryMalleablePower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryMalleablePower.class);

    public TemporaryMalleablePower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, (FuncT2<AbstractPower, AbstractCreature, Integer>) MalleablePower::new);
    }
}

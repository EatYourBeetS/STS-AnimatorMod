package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThousandCutsPower;
import eatyourbeets.interfaces.delegates.FuncT2;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryThousandCutsPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryThousandCutsPower.class);

    public TemporaryThousandCutsPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, (FuncT2<AbstractPower, AbstractCreature, Integer>) ThousandCutsPower::new);
    }
}

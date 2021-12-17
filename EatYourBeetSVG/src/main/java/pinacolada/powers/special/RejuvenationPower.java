package pinacolada.powers.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class RejuvenationPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(RejuvenationPower.class);

    public RejuvenationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        PCLActions.Bottom.Heal(amount);
    }
}

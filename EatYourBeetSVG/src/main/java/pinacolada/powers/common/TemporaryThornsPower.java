package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ThornsPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class TemporaryThornsPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryThornsPower.class);

    public TemporaryThornsPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, ThornsPower::new);
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        if (PCLGameUtilities.GetPowerAmount(owner, ThornsPower.POWER_ID) == previousAmount && previousAmount + difference == 0) {
            PCLActions.Bottom.RemovePower(owner, owner, ThornsPower.POWER_ID);
        }
    }
}

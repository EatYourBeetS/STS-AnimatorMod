package pinacolada.powers.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class SilencedPower extends PCLPower implements OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(SilencedPower.class);
    public int secondaryAmount;

    public SilencedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.priority = 99;
        Initialize(amount, PowerType.DEBUFF, true);
        updateDescription();
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        PCLActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        if (PCLGameUtilities.IsCommonBuff(power) && (power.owner == owner || target == owner)) {
            return false;
        }
        return true;
    }
}
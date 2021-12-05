package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SilencedPower extends AnimatorPower implements OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(SilencedPower.class);
    public int secondaryAmount;

    public SilencedPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;
        this.isTurnBased = true;
        this.priority = 99;
        updateDescription();
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        GameActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        if (GameUtilities.IsCommonBuff(power) && (power.owner == owner || target == owner)) {
            return false;
        }
        return true;
    }
}
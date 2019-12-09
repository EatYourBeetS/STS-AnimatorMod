package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class BiyorigoPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BiyorigoPower.class.getSimpleName());

    public BiyorigoPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (owner == target && DexterityPower.POWER_ID.equals(power.ID))
        {
            GameActions.Bottom.GainForce(amount);
            this.flashWithoutSound();
        }
    }
}

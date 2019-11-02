package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;

public class BiyorigoPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BiyorigoPower.class.getSimpleName());

    public BiyorigoPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = 0;

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (owner == target && DexterityPower.POWER_ID.equals(power.ID))
        {
            GameActionsHelper.GainForce(amount);
            this.flashWithoutSound();
        }
    }
}

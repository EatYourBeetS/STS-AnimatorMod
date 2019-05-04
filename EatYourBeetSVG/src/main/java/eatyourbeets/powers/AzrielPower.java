package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.GameActionsHelper;

public class AzrielPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(AzrielPower.class.getSimpleName());

    public AzrielPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;

        updateDescription();
    }

    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.type == PowerType.DEBUFF && !power.ID.equals(GainStrengthPower.POWER_ID) &&
            source == this.owner && target != this.owner && !target.hasPower(ArtifactPower.POWER_ID))
        {
            this.flash();
            GameActionsHelper.ApplyPower(owner, owner, new StrengthPower(owner, amount), amount);
        }
    }
}

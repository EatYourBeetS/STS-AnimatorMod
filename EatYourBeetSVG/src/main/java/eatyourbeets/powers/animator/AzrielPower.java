package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class AzrielPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(AzrielPower.class);

    public AzrielPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (power.type == PowerType.DEBUFF && !power.ID.equals(GainStrengthPower.POWER_ID) &&
            source == this.owner && !target.hasPower(ArtifactPower.POWER_ID))
        {
            this.flash();

            GameActions.Bottom.GainRandomStat(1, true);
        }
    }
}

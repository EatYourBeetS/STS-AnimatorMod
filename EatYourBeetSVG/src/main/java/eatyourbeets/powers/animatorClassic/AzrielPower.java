package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameActions;

public class AzrielPower extends AnimatorClassicPower
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

            GameActions.Bottom.GainRandomAffinityPower(1, true);
        }
    }
}

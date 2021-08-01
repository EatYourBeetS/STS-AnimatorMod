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

        Initialize(amount);
    }

    public void onApplyPower(AbstractPower p, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(p, target, source);

        if (p.type == PowerType.DEBUFF && !p.ID.equals(GainStrengthPower.POWER_ID) && source == owner && !target.hasPower(ArtifactPower.POWER_ID))
        {
            GameActions.Bottom.GainRandomAffinityPower(1, true);
            this.flash();
        }
    }
}

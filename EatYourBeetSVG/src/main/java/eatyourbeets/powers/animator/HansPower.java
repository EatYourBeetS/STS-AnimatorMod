package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.animator.beta.Hans;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class HansPower extends AnimatorPower
{
    public HansPower(AbstractCreature owner, int amount)
    {
        super(owner, Hans.DATA);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (target == null || power == null)
        {
            throw new RuntimeException("Do NOT apply a null power and/or do NOT apply it to a null target.");
        }

        if (source == owner && PoisonPower.POWER_ID.equals(power.ID) && !target.hasPower(ArtifactPower.POWER_ID))
        {
            GameActions.Bottom.GainTemporaryHP(amount);
            flash();
        }
    }
}

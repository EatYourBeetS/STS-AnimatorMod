package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.animator.series.Konosuba.Hans;
import eatyourbeets.cards.animator.status.Hans_Slimed;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class HansPower extends AnimatorPower
{
    protected int upgradedAmount = 0;

    public HansPower(AbstractCreature owner, int amount, boolean upgraded)
    {
        super(owner, Hans.DATA);

        this.amount = amount;

        if (upgraded)
        {
            this.upgradedAmount = amount;
        }

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        for (int i = 0; i < amount; i++)
        {
            AbstractCard slimed = new Hans_Slimed();
            if (i < upgradedAmount)
            {
                slimed.upgrade();
            }
            GameActions.Bottom.MakeCardInHand(slimed);
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (target == null || power == null)
        {
            throw new RuntimeException("Do NOT apply a null power and/or do NOT apply it to a null target.");
        }

        if (ID.equals(power.ID) && target == owner)
        {
            this.upgradedAmount += ((HansPower)power).upgradedAmount;
        }
        else if (source == owner && PoisonPower.POWER_ID.equals(power.ID) && !target.hasPower(ArtifactPower.POWER_ID))
        {
            GameActions.Bottom.GainTemporaryHP(amount);
            flash();
        }
    }
}

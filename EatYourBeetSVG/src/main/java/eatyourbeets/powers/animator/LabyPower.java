package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.beta.Laby;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LabyPower extends AnimatorPower
{
    protected int upgradedAmount = 0;

    public LabyPower(AbstractCreature owner, int amount, boolean upgraded)
    {
        super(owner, Laby.DATA);

        this.amount = amount;

        if (upgraded)
        {
            this.upgradedAmount = amount;
        }

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (ID.equals(power.ID) && target == owner)
        {
            this.upgradedAmount += ((LabyPower)power).upgradedAmount;
        }

        super.onApplyPower(power, target, source);
    }

    @Override
    public void updateDescription()
    {
        if (upgradedAmount > 0)
        {
            this.description = FormatDescription(1, amount, upgradedAmount);
        }
        else
        {
            this.description = FormatDescription(0, amount);
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        this.flashWithoutSound();

        GameActions.Top.ApplyConstricted(owner, owner, amount);

        if (upgradedAmount > 0)
        {
            for (AbstractMonster enemy : GameUtilities.GetAllEnemies(true))
            {
                GameActions.Top.ApplyConstricted(owner, enemy, upgradedAmount)
                .ShowEffect(false, true);
            }
        }
    }
}

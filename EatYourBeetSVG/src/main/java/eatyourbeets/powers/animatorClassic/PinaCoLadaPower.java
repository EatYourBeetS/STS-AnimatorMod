package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class PinaCoLadaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(PinaCoLadaPower.class);

    private int baseAmount;

    public PinaCoLadaPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.baseAmount = amount;
        this.amount = amount;

        updateDescription();
    }

    public void atStartOfTurn()
    {
        this.amount = this.baseAmount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        baseAmount += stackAmount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if ((card.costForTurn == 0 || card.freeToPlayOnce) && amount > 0 && !card.isInAutoplay)
        {
            amount -= 1;
            flash();
            updateDescription();

            AbstractMonster m = null;
            if (action.target != null)
            {
                m = (AbstractMonster) action.target;
            }

            GameActions.Top.PlayCopy(card, m);
        }
    }
}

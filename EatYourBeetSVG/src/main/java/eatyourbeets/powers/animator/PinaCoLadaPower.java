package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActionsHelper;

public class PinaCoLadaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(PinaCoLadaPower.class.getSimpleName());

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

        this.baseAmount += stackAmount;
    }

    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if ((card.costForTurn == 0 || card.freeToPlayOnce) && this.amount > 0 && !card.purgeOnUse)
        {
            this.amount -= 1;
            this.flash();
            updateDescription();

            AbstractMonster m = null;
            if (action.target != null)
            {
                m = (AbstractMonster) action.target;
            }

            GameActionsHelper.PlayCopy(card, m, true);
        }
    }
}

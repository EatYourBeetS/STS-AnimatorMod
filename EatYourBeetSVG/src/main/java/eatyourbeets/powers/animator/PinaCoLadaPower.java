package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PinaCoLadaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(PinaCoLadaPower.class);

    private int baseAmount;

    public PinaCoLadaPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        ResetAmount();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if ((card.costForTurn == 0 || card.freeToPlayOnce) && amount > 0 && GameUtilities.CanPlayTwice(card))
        {
            GameActions.Top.PlayCopy(card, (AbstractMonster)((action.target == null) ? null : action.target));
            this.amount -= 1;
            updateDescription();
            flash();
        }
    }
}

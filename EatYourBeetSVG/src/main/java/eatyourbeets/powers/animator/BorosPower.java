package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class BorosPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BorosPower.class);

    public BorosPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(-1);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        super.onUseCard(card, action);

        if ((card.type == AbstractCard.CardType.POWER) && GameUtilities.CanPlayTwice(card))
        {
            GameActions.Top.PlayCopy(card, (AbstractMonster)((action.target == null) ? null : action.target));
            this.flash();
        }
    }
}

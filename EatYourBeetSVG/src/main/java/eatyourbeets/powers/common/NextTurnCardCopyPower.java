package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class NextTurnCardCopyPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(NextTurnCardCopyPower.class);

    private AbstractCard card;

    public NextTurnCardCopyPower(AbstractCreature owner, AbstractCard card)
    {
        super(owner, POWER_ID);

        this.amount = 1;
        this.type = PowerType.BUFF;
        this.card = card;

        updateDescription();
    }

    public void atStartOfTurn()
    {
        flash();

        GameActions.Bottom.MakeCardInHand(card.makeStatEquivalentCopy());
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, card.name);
    }
}
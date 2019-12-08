package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.utilities.GameActionsHelper2;

import java.util.ArrayList;

public class ExhaustFromPile extends SelectFromPile
{
    public ExhaustFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.EXHAUST, sourceName, amount, groups);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            GameActionsHelper2.AddToTop(new MoveCard(card, player.exhaustPile, true));
        }

        super.Complete(result);
    }

    @Override
    protected String CreateMessage()
    {
        if (message == null)
        {
            message = ExhaustAction.TEXT[0];
        }

        return super.CreateMessage();
    }
}

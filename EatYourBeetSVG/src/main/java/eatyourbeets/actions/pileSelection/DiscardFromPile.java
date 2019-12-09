package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class DiscardFromPile extends SelectFromPile
{
    public DiscardFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.EXHAUST, sourceName, amount, groups);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            GameActions.Top.MoveCard(card, player.discardPile, true);
        }

        super.Complete(result);
    }

    @Override
    protected String CreateMessage()
    {
        if (message == null)
        {
            message = DiscardAction.TEXT[0];
        }

        return super.CreateMessage();
    }
}

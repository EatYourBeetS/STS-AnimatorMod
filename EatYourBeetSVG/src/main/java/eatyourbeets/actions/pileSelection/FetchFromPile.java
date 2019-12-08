package eatyourbeets.actions.pileSelection;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.utilities.GameActionsHelper2;

import java.util.ArrayList;

public class FetchFromPile extends SelectFromPile
{
    public FetchFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.EXHAUST, sourceName, amount, groups);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            GameActionsHelper2.AddToTop(new MoveCard(card, player.hand, true));
        }

        super.Complete(result);
    }

    @Override
    protected String CreateMessage()
    {
        if (message == null)
        {
            message = FetchAction.TEXT[0];
        }

        return super.CreateMessage();
    }
}

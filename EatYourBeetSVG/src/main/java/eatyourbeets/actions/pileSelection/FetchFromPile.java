package eatyourbeets.actions.pileSelection;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class FetchFromPile extends SelectFromPile
{
    public FetchFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.DRAW, sourceName, amount, groups);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            GameActions.Top.MoveCard(card, player.hand);
        }

        super.Complete(result);
    }

    @Override
    public String CreateMessage()
    {
        return super.CreateMessageInternal(FetchAction.TEXT[0]);
    }
}

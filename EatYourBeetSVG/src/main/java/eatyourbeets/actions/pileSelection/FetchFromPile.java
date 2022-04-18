package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class FetchFromPile extends SelectFromPile
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public FetchFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.DRAW, sourceName, amount, groups);
    }

    public FetchFromPile ShowEffect(boolean showEffect, boolean isRealtime)
    {
        this.showEffect = showEffect;
        this.realtime = isRealtime;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            GameActions.Top.MoveCard(card, player.hand).ShowEffect(showEffect, realtime);
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(GR.Common.Strings.GridSelection.Fetch_F1);
    }
}

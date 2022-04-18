package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class DiscardFromPile extends SelectFromPile
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public DiscardFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.DISCARD, sourceName, amount, groups);
    }

    public DiscardFromPile ShowEffect(boolean showEffect, boolean isRealtime)
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
            GameActions.Top.MoveCard(card, player.discardPile).ShowEffect(showEffect, realtime);
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(DiscardAction.TEXT[0]);
    }
}

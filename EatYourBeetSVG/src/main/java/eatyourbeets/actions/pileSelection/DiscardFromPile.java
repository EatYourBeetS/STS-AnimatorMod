package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.basic.MoveCard;
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
        ArrayList<MoveCard> actions = new ArrayList<>();
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, player.discardPile);
            if (showEffect)
            {
                GameActions.Top.Add(action).ShowEffect(showEffect, realtime);
            }
            else
            {
                actions.add(action);
            }
        }

        super.Complete(result);

        for (MoveCard action : actions)
        {
            action.update(); // only once
        }
    }

    @Override
    public String CreateMessage()
    {
        return super.CreateMessageInternal(DiscardAction.TEXT[0]);
    }
}

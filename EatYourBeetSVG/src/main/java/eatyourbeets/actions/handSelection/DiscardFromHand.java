package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class DiscardFromHand extends SelectFromHand
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public DiscardFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.DISCARD, sourceName, amount, isRandom);
    }

    public DiscardFromHand ShowEffect(boolean showEffect, boolean realtime)
    {
        this.showEffect = showEffect;
        this.realtime = realtime;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, player.discardPile);
            if (showEffect)
            {
                GameActions.Top.Add(action).ShowEffect(showEffect, realtime);
            }
            else
            {
                action.update(); // only once
            }
        }

        super.Complete(result);
    }

    @Override
    public String CreateMessage()
    {
        return super.CreateMessageInternal(DiscardAction.TEXT[0]);
    }
}

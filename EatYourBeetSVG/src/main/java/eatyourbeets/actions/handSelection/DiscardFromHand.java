package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class DiscardFromHand extends SelectFromHand
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public DiscardFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.DISCARD, sourceName, amount, isRandom);

        if (isRandom)
        {
            SetPriority(c -> !c.retain && !c.selfRetain);
        }
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
            GameActions.Top.MoveCard(card, player.hand, player.discardPile).ShowEffect(showEffect, realtime);
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(DiscardAction.TEXT[0]);
    }
}

package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class ExhaustFromHand extends SelectFromHand
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public ExhaustFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.EXHAUST, sourceName, amount, isRandom);
    }

    public ExhaustFromHand ShowEffect(boolean showEffect, boolean realtime)
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
            GameActions.Top.MoveCard(card, player.hand, player.exhaustPile).ShowEffect(showEffect, realtime);
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(ExhaustAction.TEXT[0]);
    }
}

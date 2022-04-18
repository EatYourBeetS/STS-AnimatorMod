package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class ExhaustFromPile extends SelectFromPile
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public ExhaustFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.EXHAUST, sourceName, amount, groups);
    }

    public ExhaustFromPile ShowEffect(boolean showEffect, boolean isRealtime)
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
            GameActions.Top.MoveCard(card, player.exhaustPile).ShowEffect(showEffect, realtime);
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(ExhaustAction.TEXT[0]);
    }
}

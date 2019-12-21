package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class DiscardFromHand extends SelectFromHand
{
    protected boolean showEffect = false;

    public DiscardFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.DISCARD, sourceName, amount, isRandom);
    }

    public DiscardFromHand ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, player.discardPile, showEffect);
            if (showEffect)
            {
                GameActions.Top.Add(action);
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

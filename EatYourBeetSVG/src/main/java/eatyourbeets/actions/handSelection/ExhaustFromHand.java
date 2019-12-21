package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class ExhaustFromHand extends SelectFromHand
{
    protected boolean showEffect = false;

    public ExhaustFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.EXHAUST, sourceName, amount, isRandom);
    }

    public ExhaustFromHand ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, player.exhaustPile, showEffect);
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
        return super.CreateMessageInternal(ExhaustAction.TEXT[0]);
    }
}

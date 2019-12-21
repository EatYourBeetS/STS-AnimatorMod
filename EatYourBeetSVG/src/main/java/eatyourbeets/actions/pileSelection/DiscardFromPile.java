package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class DiscardFromPile extends SelectFromPile
{
    protected boolean showEffect = false;

    public DiscardFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.DISCARD, sourceName, amount, groups);
    }

    public DiscardFromPile ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        ArrayList<MoveCard> actions = new ArrayList<>();
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, player.discardPile, showEffect);
            if (showEffect)
            {
                GameActions.Top.Add(action);
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

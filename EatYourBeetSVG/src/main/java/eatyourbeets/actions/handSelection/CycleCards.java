package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.handSelection.DiscardFromHand;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class CycleCards extends DiscardFromHand
{
    public CycleCards(String sourceName, int amount, boolean isRandom)
    {
        super(sourceName, amount, isRandom);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        GameActions.Bottom.Draw(result.size());

        super.Complete(result);
    }

    @Override
    public String CreateMessage()
    {
        return super.CreateMessageInternal(GamblingChipAction.TEXT[1]);
    }
}

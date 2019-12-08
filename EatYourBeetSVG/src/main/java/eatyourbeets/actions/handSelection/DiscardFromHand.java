package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class DiscardFromHand extends SelectFromHand
{
    protected boolean endOfTurn;

    public DiscardFromHand(String sourceName, int amount, boolean isRandom)
    {
        this(sourceName, amount, isRandom, false);
    }

    public DiscardFromHand(String sourceName, int amount, boolean isRandom, boolean endOfTurn)
    {
        super(ActionType.DISCARD, sourceName, amount, isRandom);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            player.hand.moveToDiscardPile(card);
            card.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(this.endOfTurn);
        }

        super.Complete(result);
    }

    @Override
    protected String CreateMessage()
    {
        message = DiscardAction.TEXT[0];

        return super.CreateMessage();
    }
}

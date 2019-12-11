package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;

public class ExhaustFromHand extends SelectFromHand
{
    public ExhaustFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.EXHAUST, sourceName, amount, isRandom);
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            player.hand.moveToExhaustPile(card);
        }

        CardCrawlGame.dungeon.checkForPactAchievement();

        super.Complete(result);
    }

    @Override
    public String CreateMessage()
    {
        return super.CreateMessageInternal(ExhaustAction.TEXT[0]);
    }
}

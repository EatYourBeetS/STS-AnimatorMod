package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class MoveRandomCardAction extends AbstractGameAction
{
    private final CardGroup destination;
    private final boolean showEffect;
    private final CardGroup source;

    public MoveRandomCardAction(CardGroup destination, CardGroup source, int amount)
    {
        this(destination, source, amount, true);
    }

    public MoveRandomCardAction(CardGroup destination, CardGroup source, int amount, boolean showEffect)
    {
        this.source = source;
        this.destination = destination;
        this.showEffect = showEffect;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        if (source.isEmpty())
        {
            if (source.type == CardGroup.CardGroupType.DRAW_PILE && AbstractDungeon.player.discardPile.size() > 0)
            {
                GameActionsHelper_Legacy.AddToTop(new EmptyDeckShuffleAction());
                GameActionsHelper_Legacy.AddToTop(new MoveRandomCardAction(destination, source, amount, showEffect));
            }
        }
        else
        {
            AbstractCard card = source.getRandomCard(AbstractDungeon.cardRandomRng);
            GameActionsHelper_Legacy.AddToTop(new MoveCard(card, destination, source, showEffect));

            if (amount > 1)
            {
                GameActionsHelper_Legacy.AddToBottom(new MoveRandomCardAction(destination, source, amount -1, showEffect));
            }
        }

        this.isDone = true;
    }
}

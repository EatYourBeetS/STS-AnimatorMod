package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.function.Predicate;

public class MoveCards extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected Predicate<AbstractCard> filter;
    protected CardSelection destination;
    protected CardSelection origin;
    protected CardGroup targetPile;
    protected CardGroup sourcePile;
    protected boolean showEffect;
    protected boolean realtime;

    public MoveCards(CardGroup targetPile, CardGroup sourcePile)
    {
        this(targetPile, sourcePile, -1);
    }

    public MoveCards(CardGroup targetPile, CardGroup sourcePile, int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        this.destination = null;
        this.origin = CardSelection.Top;
        this.targetPile = targetPile;
        this.sourcePile = sourcePile;

        Initialize(amount);
    }

    public MoveCards ShowEffect(boolean showEffect, boolean isRealtime)
    {
        this.showEffect = showEffect;
        this.realtime = isRealtime;

        return this;
    }

    public MoveCards SetOrigin(CardSelection origin)
    {
        this.origin = (origin != null ? origin : CardSelection.Top);

        return this;
    }

    public MoveCards SetDestination(CardSelection destination)
    {
        this.destination = destination;

        return this;
    }

    public MoveCards SetFilter(Predicate<AbstractCard> filter)
    {
        this.filter = filter;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        ArrayList<AbstractCard> temp = new ArrayList<>();
        for (AbstractCard card : sourcePile.group)
        {
            if (filter == null || filter.test(card))
            {
                temp.add(card);
            }
        }

        int max = amount;
        if (amount == -1 || temp.size() < amount)
        {
            max = temp.size();
        }

        boolean remove = origin.mode.IsRandom();
        for (int i = 0; i < max; i++)
        {
            AbstractCard card = origin.GetCard(temp, i, remove);
            if (card != null)
            {
                MoveCard(card);
            }
        }

        Complete(selectedCards);
    }

    private void MoveCard(AbstractCard card)
    {
        selectedCards.add(card);
        GameActions.Top.MoveCard(card, sourcePile, targetPile)
        .ShowEffect(showEffect, realtime)
        .SetDestination(destination);
    }
}

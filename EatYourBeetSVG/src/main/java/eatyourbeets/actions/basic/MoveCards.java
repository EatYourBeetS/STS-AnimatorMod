package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.function.Predicate;

public class MoveCards extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected Predicate<AbstractCard> filter;
    protected CardGroup targetPile;
    protected CardGroup sourcePile;
    protected boolean showEffect;
    protected boolean random;

    public MoveCards(CardGroup sourcePile, CardGroup targetPile)
    {
        this(targetPile, sourcePile, -1, false, false);
    }

    public MoveCards(CardGroup sourcePile, CardGroup targetPile, int amount)
    {
        this(targetPile, sourcePile, amount, false, false);
    }

    public MoveCards(CardGroup sourcePile, CardGroup targetPile, int amount, boolean showEffect, boolean random)
    {
        super(ActionType.CARD_MANIPULATION);

        this.sourcePile = sourcePile;
        this.targetPile = targetPile;
        this.showEffect = showEffect;
        this.random = random;

        Initialize(amount);
    }

    public MoveCards SetFilter(Predicate<AbstractCard> filter)
    {
        this.filter = filter;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (random)
        {
            RandomizedList<AbstractCard> temp = new RandomizedList<>();

            for (AbstractCard card : sourcePile.group)
            {
                if (filter == null || filter.test(card))
                {
                    temp.Add(card);
                }
            }

            int max = amount;
            if (amount == -1 || temp.Count() < amount)
            {
                max = temp.Count();
            }

            for (int i = 0; i < max; i++)
            {
                selectedCards.add(temp.Retrieve(AbstractDungeon.cardRandomRng));
                GameActionsHelper2.AddToTop(new MoveCard(card, targetPile, sourcePile, showEffect));
            }

            Complete(selectedCards);
        }
        else
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

            for (int i = max-1; i >= 0; i--)
            {
                selectedCards.add(temp.get(i));
                GameActionsHelper2.AddToTop(new MoveCard(card, targetPile, sourcePile, showEffect));
            }

            Complete(selectedCards);
        }
    }
}

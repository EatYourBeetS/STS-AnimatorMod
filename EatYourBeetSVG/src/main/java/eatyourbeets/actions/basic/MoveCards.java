package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActions;
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
    protected boolean realtime;
    protected boolean fromTop;
    protected boolean random;

    public MoveCards(CardGroup targetPile, CardGroup sourcePile)
    {
        this(targetPile, sourcePile, -1);
    }

    public MoveCards(CardGroup targetPile, CardGroup sourcePile, int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        this.sourcePile = sourcePile;
        this.targetPile = targetPile;

        Initialize(amount);
    }

    public MoveCards ShowEffect(boolean showEffect, boolean isRealtime)
    {
        this.showEffect = showEffect;
        this.realtime = isRealtime;

        return this;
    }

    public MoveCards SetOptions(boolean random, boolean fromTop)
    {
        this.random = random;
        this.fromTop = fromTop;

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
                MoveCard(temp.Retrieve(AbstractDungeon.cardRandomRng));
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

            for (int i = 0; i < max; i++)
            {
                if (fromTop)
                {
                    MoveCard(temp.get((temp.size() - max) + i));
                }
                else
                {
                    MoveCard(temp.get(i));
                }
            }

            Complete(selectedCards);
        }
    }

    private void MoveCard(AbstractCard card)
    {
        selectedCards.add(card);
        GameActions.Top.MoveCard(card, sourcePile, targetPile)
        .ShowEffect(showEffect, realtime);
    }
}

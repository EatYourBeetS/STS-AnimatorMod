package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class MotivateCards extends EYBActionWithCallback<AbstractCard>
{
    public static String ID = GR.Common.CreateID(MotivateCards.class.getName());

    protected final ArrayList<AbstractCard> cards = new ArrayList<>();
    protected GenericCondition<AbstractCard> filter;
    protected boolean motivateZeroCost = true;
    protected CardGroup group;
    protected boolean showEffect;
    protected int toMotivate;

    public MotivateCards(CardGroup group, int cards, int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.group = group;
        this.toMotivate = cards;

        Initialize(amount);
    }

    public MotivateCards(ArrayList<AbstractCard> cards, int amount)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.group = GameUtilities.CreateCardGroup(cards);
        this.toMotivate = group.size();

        Initialize(amount);
    }

    public MotivateCards MotivateZeroCost(boolean value)
    {
        this.motivateZeroCost = value;

        return this;
    }

    public MotivateCards SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public MotivateCards ShowEffect(boolean show)
    {
        this.showEffect = show;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (toMotivate <= 0 || amount <= 0 || group.isEmpty())
        {
            Complete();
            return;
        }

        final RandomizedList<AbstractCard> pool1 = new RandomizedList<>();
        final RandomizedList<AbstractCard> pool2 = new RandomizedList<>();

        for (AbstractCard c : group.group)
        {
            if (filter == null || filter.Check(c))
            {
                if (c.costForTurn > 0)
                {
                    pool1.Add(c);
                }
                else if (c.cost > 0)
                {
                    pool2.Add(c);
                }
            }
        }

        while (cards.size() < toMotivate)
        {
            if (pool1.Size() > 0)
            {
                cards.add(pool1.Retrieve(rng));
            }
            else if (motivateZeroCost && pool2.Size() > 0)
            {
                cards.add(pool2.Retrieve(rng));
            }
            else
            {
                break;
            }
        }

        for (int i = 0; i < cards.size(); i++)
        {
            GameActions.Top.Motivate(cards.get(i), amount)
            .ShowEffect(showEffect, i)
            .AddCallback((ActionT1<AbstractCard>) this::Complete);
        }

        Complete();
    }
}
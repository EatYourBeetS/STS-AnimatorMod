package eatyourbeets.resources.animator.misc;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RotatingList;

import java.util.ArrayList;

public class CardSlot
{
    public static final int MAX_VALUE = 30;

    public transient ArrayList<CardSlot> sharedSlots = new ArrayList<>();
    public transient RotatingList<Item> cards = new RotatingList<>();
    public Item selected;
    public int amount;
    public int max;
    public int min;

    public CardSlot(int min, int max)
    {
        if (min > max)
        {
            throw new RuntimeException("Min can't be greater than max.");
        }

        this.min = min;
        this.max = max;
    }

    public EYBCardData GetData()
    {
        return selected != null ? selected.data : null;
    }

    public AbstractCard GetCard()
    {
        return selected != null ? selected.GetCard() : null;
    }

    public EYBCardAffinities GetAffinities()
    {
        EYBCard card = JUtils.SafeCast(GetCard(), EYBCard.class);
        return card != null ? card.affinities : null;
    }

    public int GetEstimatedValue()
    {
        return amount * (selected != null ? selected.estimatedValue : 0);
    }

    public void Next()
    {
        if (selected == null)
        {
            Select(cards.Current());
        }
        else
        {
            Select(cards.Next(true));
        }

        int i = 0;
        while (true)
        {
            int currentIndex = i;
            for (CardSlot s : sharedSlots)
            {
                if (s != this && selected.data == s.GetData())
                {
                    Select(cards.Next(true));
                    i += 1;
                    break;
                }
            }

            if (currentIndex == i)
            {
                return;
            }
            else if (i >= cards.Count())
            {
                Select(null);
                return;
            }
        }
    }

    public boolean CanAdd()
    {
        return (selected != null) && amount < max;
    }

    public boolean CanRemove()
    {
        return (selected != null) && amount > min;
    }

    public void Add()
    {
        if (amount < max)
        {
            amount += 1;
        }
    }

    public void Remove()
    {
        if (amount > 1)
        {
            amount -= 1;
        }
        else if (min <= 0)
        {
            Select(null);
        }
    }

    public void AddSharedSlot(CardSlot other)
    {
        if (other != this && !sharedSlots.contains(other))
        {
            sharedSlots.add(other);
        }
    }

    public void AddItem(EYBCardData data, int estimatedValue)
    {
        cards.Add(new Item(data, estimatedValue));

        if (min > 0 && selected == null)
        {
            Select(cards.Current());
        }
    }

    public void Select(Item item)
    {
        selected = item;
        if (item == null)
        {
            if (min > 0)
            {
                throw new RuntimeException("Tried to deselect an item, but at least 1 card needs to be selected.");
            }
            amount = 0;
        }
        else
        {
            if (max <= 0)
            {
                throw new RuntimeException("Tried to select an item, but no cards are allowed in this slot.");
            }
            amount = MathUtils.clamp(amount, min == 0 ? 1 : min, max);
        }
    }

    public class Item
    {
        public EYBCardData data;
        public AbstractCard card;
        public int estimatedValue;

        public Item(EYBCardData data, int estimatedValue)
        {
            this.data = data;
            this.estimatedValue = estimatedValue;
        }

        public AbstractCard GetCard()
        {
            if (card == null)
            {
                card = data.CreateNewInstance();
            }

            return card;
        }
    }
}

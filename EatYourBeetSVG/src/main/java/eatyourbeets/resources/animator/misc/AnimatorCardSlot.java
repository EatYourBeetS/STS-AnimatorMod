package eatyourbeets.resources.animator.misc;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RotatingList;

import java.util.ArrayList;

public class AnimatorCardSlot
{
    public transient final AnimatorLoadoutData Container;
    public transient final RotatingList<Item> Cards;

    public Item selected;
    public int amount;
    public int max;
    public int min;

    public AnimatorCardSlot(AnimatorLoadoutData container, int min, int max)
    {
        if (min > max)
        {
            throw new RuntimeException("Min can't be greater than max.");
        }

        this.Cards = new RotatingList<>();
        this.Container = container;
        this.min = min;
        this.max = max;
    }

    public int GetSlotIndex()
    {
        return Container.cardSlots.indexOf(this);
    }

    public EYBCardData GetData()
    {
        return selected != null ? selected.data : null;
    }

    public EYBCard GetCard(boolean refresh)
    {
        return selected != null ? selected.GetCard(refresh) : null;
    }

    public EYBCardAffinities GetAffinities()
    {
        EYBCard card = JUtils.SafeCast(GetCard(false), EYBCard.class);
        return card != null ? card.affinities : null;
    }

    public int GetEstimatedValue()
    {
        return amount * (selected == null ? 0 : selected.estimatedValue);
    }

    public ArrayList<EYBCard> GetSelectableCards()
    {
        final ArrayList<EYBCard> cards = new ArrayList<>();
        for (Item item : Cards)
        {
            boolean add = true;
            for (AnimatorCardSlot slot : Container)
            {
                if (slot != this && slot.GetData() == item.data)
                {
                    add = false;
                }
            }

            if (add)
            {
                cards.add(item.GetCard(true));
            }
        }

        return cards;
    }

    public AnimatorCardSlot MakeCopy(AnimatorLoadoutData container)
    {
        final AnimatorCardSlot copy = new AnimatorCardSlot(container, min, max);
        for (Item item : Cards)
        {
            copy.Cards.Add(item);
        }
        if (selected != null)
        {
            copy.Select(selected.data, amount);
        }

        return copy;
    }

    public void Next()
    {
        if (selected == null)
        {
            Select(Cards.Current());
        }
        else
        {
            Select(Cards.Next(true));
        }

        int i = 0;
        while (true)
        {
            int currentIndex = i;
            for (AnimatorCardSlot s : Container)
            {
                if (s != this && selected.data == s.GetData())
                {
                    Select(Cards.Next(true));
                    i += 1;
                    break;
                }
            }

            if (currentIndex == i)
            {
                return;
            }
            else if (i >= Cards.Count())
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

    public void AddItem(EYBCardData data, int estimatedValue)
    {
        Cards.Add(new Item(data, estimatedValue));
    }

    public void AddItems(ArrayList<EYBCardData> items, int estimatedValue)
    {
        this.AddItems(items, estimatedValue, estimatedValue);
    }

    public void AddItems(ArrayList<EYBCardData> items, int estimatedValue, int estimatedSpecialValue)
    {
        for (EYBCardData data : items)
        {
            if (data.CardRarity == AbstractCard.CardRarity.SPECIAL)
            {
                Cards.Add(new Item(data, estimatedSpecialValue));
            }
            else {
                Cards.Add(new Item(data, estimatedValue));
            }
        }
    }

    public AnimatorCardSlot Select(Item item)
    {
        return Select(item, item == null ? 0 : 1);
    }

    public AnimatorCardSlot Select(EYBCardData data, int amount)
    {
        int i = 0;
        for (Item item : Cards)
        {
            if (item.data == data)
            {
                return Select(i, amount);
            }
            i += 1;
        }

        return null;
    }

    public AnimatorCardSlot Select(int index, int amount)
    {
        return Select(Cards.SetIndex(index), amount);
    }

    public AnimatorCardSlot Select(Item item, int amount)
    {
        selected = item;
        if (item == null)
        {
            if (min > 0)
            {
                throw new RuntimeException("Tried to deselect an item, but at least 1 card needs to be selected.");
            }
            this.amount = 0;
        }
        else
        {
            if (max <= 0)
            {
                throw new RuntimeException("Tried to select an item, but no cards are allowed in this slot.");
            }
            this.amount = MathUtils.clamp(amount, min, max);
        }

        return this;
    }

    public class Item
    {
        public final EYBCardData data;
        public final int estimatedValue;

        protected EYBCard card;

        public Item(EYBCardData data, int estimatedValue)
        {
            this.data = data;
            this.estimatedValue = estimatedValue;
        }

        public EYBCard GetCard(boolean forceRefresh)
        {
            if (card == null || forceRefresh)
            {
                EYBCard eCard = JUtils.SafeCast(CardLibrary.getCard(data.ID), EYBCard.class);
                if (eCard != null) {
                    card = (EYBCard) CardLibrary.getCard(data.ID).makeCopy();
                    if (data.IsNotSeen())
                    {
                        card.isSeen = false;
                    }
                }
            }

            return card;
        }
    }
}

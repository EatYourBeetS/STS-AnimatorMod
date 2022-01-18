package pinacolada.resources.pcl.misc;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.utilities.RotatingList;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class PCLCardSlot
{
    public static final int MAX_LIMIT = 6;
    public transient final PCLLoadoutData Container;
    public transient final RotatingList<Item> Cards;

    public Item selected;
    public int amount;
    public int current_max;
    public int max;
    public int min;

    public PCLCardSlot(PCLLoadoutData container)
    {
        this(container,0,MAX_LIMIT);
    }

    public PCLCardSlot(PCLLoadoutData container, int min, int max)
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

    public PCLCardData GetData()
    {
        return selected != null ? selected.data : null;
    }

    public PCLCard GetCard(boolean refresh)
    {
        return selected != null ? selected.GetCard(refresh) : null;
    }

    public PCLCardAffinities GetAffinities()
    {
        PCLCard card = PCLJUtils.SafeCast(GetCard(false), PCLCard.class);
        return card != null ? card.affinities : null;
    }

    public int GetEstimatedValue()
    {
        return amount * (selected == null ? 0 : selected.estimatedValue);
    }

    public ArrayList<PCLCard> GetSelectableCards()
    {
        final ArrayList<PCLCard> cards = new ArrayList<>();
        for (Item item : Cards)
        {
            boolean add = true;
            for (PCLCardSlot slot : Container.cardSlots)
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

    public PCLCardSlot MakeCopy(PCLLoadoutData container)
    {
        final PCLCardSlot copy = new PCLCardSlot(container, min, max);
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
            for (PCLCardSlot s : Container.cardSlots)
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
        return (selected != null) && amount < max && amount < current_max;
    }

    public boolean CanDecrement()
    {
        return (selected != null) && amount > 1 && amount > min;
    }

    public boolean CanRemove()
    {
        return (selected != null) && min <= 0;
    }

    public void Add()
    {
        if (amount < max && amount < current_max)
        {
            amount += 1;
        }
    }

    public void Decrement()
    {
        if (amount > 1)
        {
            amount -= 1;
        }
    }

    public void AddItem(PCLCardData data, int estimatedValue)
    {
        Cards.Add(new Item(data, estimatedValue));
    }

    public void AddItems(ArrayList<PCLCardData> items, int estimatedValue)
    {
        for (PCLCardData data : items)
        {
            Cards.Add(new Item(data, estimatedValue));
        }
    }

    public PCLCardSlot Clear() {
        return Select(null);
    }

    public PCLCardSlot Select(Item item)
    {
        return Select(item, item == null ? 0 : 1);
    }

    public PCLCardSlot Select(PCLCardData data, int amount)
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

    public PCLCardSlot Select(int index, int amount)
    {
        return Select(Cards.SetIndex(index), amount);
    }

    public PCLCardSlot Select(Item item, int amount)
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

            current_max = Math.min(max, selected.data.MaxCopies >= min ? selected.data.MaxCopies : max);
            if (current_max <= 0) {
                current_max = MAX_LIMIT;
            }
            this.amount = MathUtils.clamp(amount, min, current_max);
        }

        return this;
    }

    public static class Item
    {
        public final PCLCardData data;
        public final int estimatedValue;

        protected PCLCard card;

        public Item(PCLCardData data, int estimatedValue)
        {
            this.data = data;
            this.estimatedValue = estimatedValue;
        }

        public PCLCard GetCard(boolean forceRefresh)
        {
            if (card == null || forceRefresh)
            {
                PCLCard eCard = PCLJUtils.SafeCast(CardLibrary.getCard(data.ID), PCLCard.class);
                if (eCard != null) {
                    card = (PCLCard) CardLibrary.getCard(data.ID).makeCopy();
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

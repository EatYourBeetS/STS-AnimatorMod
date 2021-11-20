package eatyourbeets.resources.animator.misc;

import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.RotatingList;

import java.util.ArrayList;

public class AnimatorRelicSlot
{
    public transient final AnimatorLoadoutData Container;
    public transient final RotatingList<Item> Relics;

    public Item selected;
    public AnimatorRelicSlot(AnimatorLoadoutData container)
    {

        this.Relics = new RotatingList<>();
        this.Container = container;
    }

    public int GetSlotIndex()
    {
        return Container.relicSlots.indexOf(this);
    }

    public EYBRelic GetRelic()
    {
        return selected != null ? selected.relic : null;
    }

    public int GetEstimatedValue()
    {
        return (selected == null ? 0 : selected.estimatedValue);
    }

    public ArrayList<AnimatorRelicSlot.Item> GetSelectableRelics()
    {
        final ArrayList<AnimatorRelicSlot.Item> relics = new ArrayList<>();
        for (Item item : Relics)
        {
            boolean add = true;
            for (AnimatorRelicSlot slot : Container.relicSlots)
            {
                if (slot != this && slot.GetRelic() == item.relic)
                {
                    add = false;
                }
            }

            if (add)
            {
                relics.add(new AnimatorRelicSlot.Item(item.relic, item.estimatedValue));
            }
        }

        return relics;
    }

    public AnimatorRelicSlot MakeCopy(AnimatorLoadoutData container)
    {
        final AnimatorRelicSlot copy = new AnimatorRelicSlot(container);
        for (Item item : Relics)
        {
            copy.Relics.Add(item);
        }
        if (selected != null)
        {
            copy.Select(selected.relic);
        }

        return copy;
    }

    public void Next()
    {
        if (selected == null)
        {
            Select(Relics.Current());
        }
        else
        {
            Select(Relics.Next(true));
        }

        int i = 0;
        while (true)
        {
            int currentIndex = i;
            for (AnimatorRelicSlot s : Container.relicSlots)
            {
                if (s != this && selected.relic == s.GetRelic())
                {
                    Select(Relics.Next(true));
                    i += 1;
                    break;
                }
            }

            if (currentIndex == i)
            {
                return;
            }
            else if (i >= Relics.Count())
            {
                Select((EYBRelic) null);
                return;
            }
        }
    }

    public boolean CanRemove()
    {
        return (selected != null);
    }

    public AnimatorRelicSlot Clear() {
        selected = null;
        return this;
    }

    public void AddItem(EYBRelic relic, int estimatedValue)
    {
        Relics.Add(new Item(relic, estimatedValue));
    }

    public void AddItems(ArrayList<EYBRelic> items, int estimatedValue)
    {
        for (EYBRelic data : items)
        {
            Relics.Add(new Item(data, estimatedValue));
        }
    }

    public AnimatorRelicSlot Select(EYBRelic relic)
    {
        int i = 0;
        for (Item item : Relics)
        {
            if (item.relic == relic)
            {
                return Select(i);
            }
            i += 1;
        }

        return null;
    }

    public AnimatorRelicSlot Select(int index)
    {
        return Select(Relics.SetIndex(index));
    }

    public AnimatorRelicSlot Select(Item item)
    {
        selected = item;
        return this;
    }

    public static class Item
    {
        public final EYBRelic relic;
        public final int estimatedValue;

        public Item(EYBRelic relic, int estimatedValue)
        {
            this.relic = relic;
            this.estimatedValue = estimatedValue;
        }
    }
}

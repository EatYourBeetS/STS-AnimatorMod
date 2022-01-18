package pinacolada.resources.pcl.misc;

import eatyourbeets.utilities.RotatingList;
import pinacolada.relics.PCLRelic;

import java.util.ArrayList;

public class PCLRelicSlot
{
    public transient final PCLLoadoutData Container;
    public transient final RotatingList<Item> Relics;

    public Item selected;
    public PCLRelicSlot(PCLLoadoutData container)
    {

        this.Relics = new RotatingList<>();
        this.Container = container;
    }

    public int GetSlotIndex()
    {
        return Container.relicSlots.indexOf(this);
    }

    public PCLRelic GetRelic()
    {
        return selected != null ? selected.relic : null;
    }

    public int GetEstimatedValue()
    {
        return (selected == null ? 0 : selected.estimatedValue);
    }

    public ArrayList<PCLRelicSlot.Item> GetSelectableRelics()
    {
        final ArrayList<PCLRelicSlot.Item> relics = new ArrayList<>();
        for (Item item : Relics)
        {
            boolean add = true;
            for (PCLRelicSlot slot : Container.relicSlots)
            {
                if (slot != this && slot.GetRelic() == item.relic)
                {
                    add = false;
                }
            }

            if (add)
            {
                relics.add(new PCLRelicSlot.Item(item.relic, item.estimatedValue));
            }
        }

        return relics;
    }

    public PCLRelicSlot MakeCopy(PCLLoadoutData container)
    {
        final PCLRelicSlot copy = new PCLRelicSlot(container);
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
            for (PCLRelicSlot s : Container.relicSlots)
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
                Select((PCLRelic) null);
                return;
            }
        }
    }

    public boolean CanRemove()
    {
        return (selected != null);
    }

    public PCLRelicSlot Clear() {
        selected = null;
        return this;
    }

    public void AddItem(PCLRelic relic, int estimatedValue)
    {
        Relics.Add(new Item(relic, estimatedValue));
    }

    public void AddItems(ArrayList<PCLRelic> items, int estimatedValue)
    {
        for (PCLRelic data : items)
        {
            Relics.Add(new Item(data, estimatedValue));
        }
    }

    public PCLRelicSlot Select(PCLRelic relic)
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

    public PCLRelicSlot Select(int index)
    {
        return Select(Relics.SetIndex(index));
    }

    public PCLRelicSlot Select(Item item)
    {
        selected = item;
        return this;
    }

    public static class Item
    {
        public final PCLRelic relic;
        public final int estimatedValue;

        public Item(PCLRelic relic, int estimatedValue)
        {
            this.relic = relic;
            this.estimatedValue = estimatedValue;
        }
    }
}

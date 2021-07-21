package eatyourbeets.resources.animator.misc;

import java.util.ArrayList;
import java.util.Iterator;

public class CardSlots implements Iterable<CardSlot>
{
    public boolean Ready;

    private final ArrayList<CardSlot> list = new ArrayList<>();

    public CardSlot AddSlot(int min, int max)
    {
        CardSlot slot = new CardSlot(this, min, max);
        list.add(slot);
        return slot;
    }

    public CardSlots MakeCopy()
    {
        CardSlots copy = new CardSlots();
        copy.Ready = Ready;
        for (CardSlot slot : list)
        {
            copy.list.add(slot.MakeCopy(copy));
        }
        return copy;
    }

    public CardSlot Get(int index)
    {
        return list.get(index);
    }

    public int Size()
    {
        return list.size();
    }

    @Override
    public Iterator<CardSlot> iterator()
    {
        return list.iterator();
    }
}

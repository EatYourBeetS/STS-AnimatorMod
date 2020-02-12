package eatyourbeets.utilities;

import java.util.ArrayList;
import java.util.Collection;

public class RotatingList<T>
{
    private final ArrayList<T> items;
    private int index;

    public RotatingList()
    {
        items = new ArrayList<>();
    }

    public RotatingList(Collection<? extends T> collection)
    {
        items = new ArrayList<>(collection);
    }

    public void Add(T item)
    {
        items.add(item);
    }

    public void AddAll(ArrayList<T> list)
    {
        items.addAll(list);
    }

    public void Clear()
    {
        items.clear();
    }

    public int Count()
    {
        return items.size();
    }

    public boolean Remove(T item)
    {
        return items.remove(item);
    }

    public ArrayList<T> GetInnerList()
    {
        return items;
    }

    public T Current()
    {
        return items.isEmpty() ? null : items.get(index);
    }

    public T Previous(boolean moveIndex)
    {
        int newIndex = index - 1;
        if (newIndex < 0)
        {
            newIndex = items.size() - 1;
        }

        if (moveIndex)
        {
            index = newIndex;
        }

        return items.get(newIndex);
    }

    public T Next(boolean moveIndex)
    {
        int newIndex = index + 1;
        if (newIndex >= items.size())
        {
            newIndex = 0;
        }

        if (moveIndex)
        {
            index = newIndex;
        }

        return items.get(newIndex);
    }
}

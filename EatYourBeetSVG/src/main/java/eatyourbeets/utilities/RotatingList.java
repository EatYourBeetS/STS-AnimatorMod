package eatyourbeets.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RotatingList<T> implements Iterable<T>
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

    public int GetIndex()
    {
        return index;
    }

    public boolean Remove(T item)
    {
        return items.remove(item);
    }

    public ArrayList<T> GetInnerList()
    {
        return items;
    }

    public T SetIndex(int index)
    {
        this.index = index < 0 ? 0 : index < items.size() ? index : items.size() - 1;
        return Current();
    }

    public T Current()
    {
        return Current(false);
    }

    public T Current(boolean moveIndex)
    {
        T item = items.isEmpty() ? null : items.get(index);
        if (moveIndex)
        {
            Next(true);
        }

        return item;
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

    @Override
    public Iterator<T> iterator()
    {
        return items.iterator();
    }
}

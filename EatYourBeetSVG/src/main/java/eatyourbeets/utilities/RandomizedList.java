package eatyourbeets.utilities;

import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.Collection;

public class RandomizedList<T>
{
    private final ArrayList<T> items;

    public RandomizedList()
    {
        items = new ArrayList<>();
    }

    public RandomizedList(Collection<? extends T> collection)
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

    public int Size() { return items.size(); }

    public boolean Remove(T item)
    {
        return items.remove(item);
    }

    public T Retrieve(Random rng, boolean remove)
    {
        T item = JUtils.GetRandomElement(items, rng);
        if (remove)
        {
            items.remove(item);
        }

        return item;
    }

    public T Retrieve(Random rng)
    {
        return Retrieve(rng, true);
    }

    public ArrayList<T> GetInnerList()
    {
        return items;
    }
}

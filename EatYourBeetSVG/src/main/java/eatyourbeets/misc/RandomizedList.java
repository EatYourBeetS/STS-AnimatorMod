package eatyourbeets.misc;

import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.Utilities;

import java.util.ArrayList;

public class RandomizedList<T>
{
    private final ArrayList<T> items;

    public RandomizedList()
    {
        items = new ArrayList<>();
    }

    public RandomizedList(ArrayList<T> list)
    {
        items = new ArrayList<>(list);
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

    public int Count() { return items.size(); }

    public boolean Remove(T item)
    {
        return items.remove(item);
    }

    public T Retrieve(Random rng, boolean remove)
    {
        T item = Utilities.GetRandomElement(items, rng);
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

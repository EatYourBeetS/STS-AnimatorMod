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

    public T Retrieve(Random rng)
    {
        T item = Utilities.GetRandomElement(items, rng);
        items.remove(item);

        return item;
    }
}

package eatyourbeets.utilities;

import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;

public class WeightedList<T>
{
    private class Item
    {
        final int weight;
        final T object;

        private Item(T object, int weight)
        {
            this.weight = weight;
            this.object = object;
        }
    }

    private final List<Item> items;
    private int totalWeight;

    public WeightedList()
    {
        totalWeight = 0;
        items = new ArrayList<>();
    }

    public WeightedList(WeightedList<T> copy)
    {
        this();

        for (Item item : copy.items)
        {
            Add(item.object, item.weight);
        }
    }

    public List<T> GetInnerList()
    {
        final ArrayList<T> result = new ArrayList<>();
        for (Item item : items)
        {
            result.add(item.object);
        }

        return result;
    }

    public int Size()
    {
        return items.size();
    }

    public void Clear()
    {
        items.clear();
        totalWeight = 0;
    }

    public void Add(T object, int weight)
    {
        totalWeight += weight;
        items.add(new Item(object, weight));
    }

    public void AddAll(T[] list, int weight)
    {
        for (T object : list) {
            totalWeight += weight;
            items.add(new Item(object, weight));
        }
    }

    public void AddAll(Iterable<T> list, int weight)
    {
        for (T object : list) {
            totalWeight += weight;
            items.add(new Item(object, weight));
        }
    }

    public T Retrieve(Random rng)
    {
        return Retrieve(rng, true);
    }

    public T Retrieve(Random rng, boolean remove)
    {
        return Retrieve(rng.random(totalWeight), remove);
    }

    public T RetrieveUnseeded(boolean remove)
    {
        return Retrieve(JUtils.RNG.nextInt(totalWeight + 1), remove);
    }

    private T Retrieve(int roll, boolean remove)
    {
        Item selected = null;
        int currentWeight = 0;
        for (Item item : items)
        {
            if ((currentWeight + item.weight) >= roll)
            {
                selected = item;
                break;
            }

            currentWeight += item.weight;
        }

        if (selected == null)
        {
            return null;
        }

        if (remove)
        {
            Remove(selected);
        }

        return selected.object;
    }

    private void Remove(Item item)
    {
        totalWeight -= item.weight;
        items.remove(item);
    }
}

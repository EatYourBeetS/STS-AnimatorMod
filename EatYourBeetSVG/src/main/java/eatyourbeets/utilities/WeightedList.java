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

    public T Retrieve(Random rng)
    {
        return Retrieve(rng, true);
    }

    public T Retrieve(Random rng, boolean remove)
    {
        int r = rng.random(totalWeight);
        int currentWeight = 0;

        Item selected = null;
        for (Item item : items)
        {
            if ((currentWeight + item.weight) >= r)
            {
                selected = item;

                break;
            }
            currentWeight += item.weight;
        }

        if (selected != null)
        {
            if (remove)
            {
                Remove(selected);
            }

            return selected.object;
        }
        else
        {
            return null;
        }
    }

    private void Remove(Item item)
    {
        totalWeight -= item.weight;
        items.remove(item);
    }
}

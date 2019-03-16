package eatyourbeets.misc;

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

    public int Count()
    {
        return items.size();
    }

    public void Add(T object, int weight)
    {
        totalWeight += weight;
        items.add(new Item(object, weight));
    }

    public T Retrieve(Random rng)
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
            Remove(selected);
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

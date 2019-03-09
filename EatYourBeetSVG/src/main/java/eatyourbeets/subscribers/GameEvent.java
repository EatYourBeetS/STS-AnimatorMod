package eatyourbeets.subscribers;

import java.util.ArrayList;

public class GameEvent<T>
{
    private final ArrayList<T> subscribersCopy = new ArrayList<>();
    private final ArrayList<T> subscribers = new ArrayList<>();

    public int Count()
    {
        return subscribers.size();
    }

    public ArrayList<T> GetSubscribers()
    {
        subscribersCopy.clear();
        subscribersCopy.addAll(subscribers);

        return subscribersCopy;
    }

    public void Clear()
    {
        subscribersCopy.clear();
        subscribers.clear();
    }

    public void Unsubscribe(T subscriber)
    {
        subscribers.remove(subscriber);
    }

    public void Subscribe(T subscriber)
    {
        if (!subscribers.contains(subscriber))
        {
            subscribers.add(subscriber);
        }
    }
}

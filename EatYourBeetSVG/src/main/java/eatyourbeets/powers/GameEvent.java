package eatyourbeets.powers;

import java.util.ArrayList;

public class GameEvent<T>
{
    private final ArrayList<T> subscribersCopy = new ArrayList<>();
    private final ArrayList<T> subscribers = new ArrayList<>();
    private final ArrayList<T> oneTimeSubscribers = new ArrayList<>();

    public int Count()
    {
        return subscribers.size() + oneTimeSubscribers.size();
    }

    public ArrayList<T> GetSubscribers()
    {
        subscribersCopy.clear();
        subscribersCopy.addAll(subscribers);
        subscribersCopy.addAll(oneTimeSubscribers);

        oneTimeSubscribers.clear();

        return subscribersCopy;
    }

    public void Clear()
    {
        oneTimeSubscribers.clear();
        subscribersCopy.clear();
        subscribers.clear();
    }

    public boolean Unsubscribe(T subscriber)
    {
        return subscribers.remove(subscriber);
    }

    public boolean Subscribe(T subscriber)
    {
        return !subscribers.contains(subscriber) && subscribers.add(subscriber);
    }

    public boolean SubscribeOnce(T subscriber)
    {
        return !oneTimeSubscribers.contains(subscriber) && oneTimeSubscribers.add(subscriber);
    }
}

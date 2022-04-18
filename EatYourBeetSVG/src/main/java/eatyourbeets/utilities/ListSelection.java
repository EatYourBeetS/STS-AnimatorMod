package eatyourbeets.utilities;

import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT3;

import java.util.ArrayList;
import java.util.List;

public abstract class ListSelection<Item>
{
    public enum Mode
    {
        Random,
        Last,
        First,
        Special;

        //@Formatter: Off
        public boolean IsFirst()  { return First.equals(this);  }
        public boolean IsRandom()  { return Random.equals(this);  }
        public boolean IsLast()     { return Last.equals(this);     }
        public boolean IsSpecial() { return Special.equals(this); }
        //@Formatter: On
    }

    public static final ListSelection<Object> Last = Last(0);
    public static final ListSelection<Object> First = First(0);
    public static final ListSelection<Object> Random = Random(null);
    public static final ListSelection<Object> Default = Last;

    public final Mode mode;

    public abstract void Add(List<Item> list, Item item, int index);
    public abstract Item Get(List<Item> list, int index, boolean remove);

    protected ListSelection(Mode mode)
    {
        this.mode = mode;
    }

    public static <T> ListSelection<T> Default(int shift)
    {
        return Last(shift);
    }

    public static <T> ListSelection<T> Last(int shift)
    {
        return new Last<>(shift);
    }

    public static <T> ListSelection<T> First(int shift)
    {
        return new First<>(shift);
    }

    public static <T> ListSelection<T> Random(com.megacrit.cardcrawl.random.Random rng)
    {
        return new Random<>(rng);
    }

    public static <T> ListSelection<T> Special(ActionT3<List<T>, T, Integer> add,
                                     FuncT3<T, List<T>, Integer, Boolean> get)
    {
        return new Special<>(add, get);
    }

    public void ForEach(ArrayList<Item> modifiableList, int amount, ActionT1<Item> apply)
    {
        final boolean remove = mode.IsRandom();
        final int max = Math.min(modifiableList.size(), amount);
        for (int i = 0; i < max; i++)
        {
            Item card = Get(modifiableList, i, remove);
            if (card != null)
            {
                apply.Invoke(card);
            }
        }
    }

    private static class First<T> extends ListSelection<T>
    {
        private final int shift;

        public First(int shift)
        {
            super(Mode.First);

            this.shift = shift;
        }

        @Override
        public void Add(List<T> list, T item, int index)
        {
            list.add(Math.max(0, Math.min(list.size(), index + shift)), item);
        }

        @Override
        public T Get(List<T> list, int index, boolean remove)
        {
            T card = null;
            int position = index + shift;
            if (position >= 0 && position < list.size())
            {
                card = list.get(position);
                if (remove)
                {
                    list.remove(position);
                }
            }

            return card;
        }
    }

    private static class Last<T> extends ListSelection<T>
    {
        private final int shift;

        public Last(int shift)
        {
            super(Mode.Last);

            this.shift = shift;
        }

        @Override
        public void Add(List<T> list, T item, int index)
        {
            list.add(Math.max(0, Math.min(list.size(), list.size() - index - shift)), item);
        }

        @Override
        public T Get(List<T> list, int index, boolean remove)
        {
            T card = null;
            int position = list.size() - 1 - index - shift;
            if (position >= 0 && position < list.size())
            {
                card = list.get(position);
                if (remove)
                {
                    list.remove(position);
                }
            }

            return card;
        }
    }

    private static class Random<T> extends ListSelection<T>
    {
        private final com.megacrit.cardcrawl.random.Random rng;

        private Random(com.megacrit.cardcrawl.random.Random rng)
        {
            super(Mode.Random);

            this.rng = rng;
        }

        protected int GetRandomIndex(int size)
        {
            if (size <= 0)
            {
                return 0;
            }
            else if (rng == null)
            {
                return GameUtilities.GetRNG().random(size);
            }
            else
            {
                return rng.random(size);
            }
        }

        @Override
        public void Add(List<T> list, T item, int index)
        {
            list.add(GetRandomIndex(list.size() - 1), item);
        }

        @Override
        public T Get(List<T> list, int index, boolean remove)
        {
            T card = null;
            if (list.size() > 0)
            {
                int position = GetRandomIndex(list.size() - 1);
                card = list.get(position);
                if (remove)
                {
                    list.remove(position);
                }
            }

            return card;
        }
    }

    private static class Special<T> extends ListSelection<T>
    {
        private ActionT3<List<T>, T, Integer> addCard;
        private FuncT3<T, List<T>, Integer, Boolean> getCard;

        public Special(ActionT3<List<T>, T, Integer> add,
                FuncT3<T, List<T>, Integer, Boolean> get)
        {
            super(Mode.Special);

            this.addCard = add;
            this.getCard = get;
        }

        @Override
        public void Add(List<T> list, T item, int index)
        {
            addCard.Invoke(list, item, index);
        }

        @Override
        public T Get(List<T> list, int index, boolean remove)
        {
            return getCard.Invoke(list, index, remove);
        }
    }
}

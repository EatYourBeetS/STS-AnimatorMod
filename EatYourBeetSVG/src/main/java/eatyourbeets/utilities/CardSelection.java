package eatyourbeets.utilities;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT3;

import java.util.List;

public class CardSelection
{
    public static final ListSelection<AbstractCard> Top = ListSelection.Last(0);
    public static final ListSelection<AbstractCard> Bottom = ListSelection.First(0);
    public static final ListSelection<AbstractCard> Random = ListSelection.Random(null);
    public static final ListSelection<AbstractCard> Default = Top;

    public static ListSelection<AbstractCard> Default(int shift)
    {
        return Top(shift);
    }

    public static ListSelection<AbstractCard> Top(int shift)
    {
        return ListSelection.Last(shift);
    }

    public static ListSelection<AbstractCard> Bottom(int shift)
    {
        return ListSelection.First(shift);
    }

    public static ListSelection<AbstractCard> Random(com.megacrit.cardcrawl.random.Random rng)
    {
        return ListSelection.Random(rng);
    }

    public static ListSelection<AbstractCard> Special(ActionT3<List<AbstractCard>, AbstractCard, Integer> add,
                                               FuncT3<AbstractCard, List<AbstractCard>, Integer, Boolean> get)
    {
        return ListSelection.Special(add, get);
    }
}

//public abstract class CardSelection
//{
//    public enum Mode
//    {
//        Random,
//        Top,
//        Bottom,
//        Special;
//
//        //@Formatter: Off
//        public boolean IsBottom()  { return Bottom.equals(this);  }
//        public boolean IsRandom()  { return Random.equals(this);  }
//        public boolean IsTop()     { return Top.equals(this);     }
//        public boolean IsSpecial() { return Special.equals(this); }
//        //@Formatter: On
//    }
//
//    public static final CardSelection Top = Top(0);
//    public static final CardSelection Bottom = Bottom(0);
//    public static final CardSelection Random = Random(null);
//    public static final CardSelection Default = Top;
//
//    public final Mode mode;
//
//    public abstract void AddCard(List<AbstractCard> list, AbstractCard card, int index);
//    public abstract AbstractCard GetCard(List<AbstractCard> list, int index, boolean remove);
//
//    protected CardSelection(Mode mode)
//    {
//        this.mode = mode;
//    }
//
//    public static CardSelection Top(int shift)
//    {
//        return new Top(shift);
//    }
//
//    public static CardSelection Bottom(int shift)
//    {
//        return new Bottom(shift);
//    }
//
//    public static CardSelection Random(com.megacrit.cardcrawl.random.Random rng)
//    {
//        return new Random(rng);
//    }
//
//    public static CardSelection Special(ActionT3<List<AbstractCard>, AbstractCard, Integer> addCard,
//                                        FuncT3<AbstractCard, List<AbstractCard>, Integer, Boolean> getCard)
//    {
//        return new Special(addCard, getCard);
//    }
//
//    public void ForEach(ArrayList<AbstractCard> modifiableList, int amount, ActionT1<AbstractCard> apply)
//    {
//        final boolean remove = mode.IsRandom();
//        final int max = Math.min(modifiableList.size(), amount);
//        for (int i = 0; i < max; i++)
//        {
//            AbstractCard card = GetCard(modifiableList, i, remove);
//            if (card != null)
//            {
//                apply.Invoke(card);
//            }
//        }
//    }
//
//    private static class Bottom extends CardSelection
//    {
//        private final int shift;
//
//        public Bottom(int shift)
//        {
//            super(Mode.Bottom);
//
//            this.shift = shift;
//        }
//
//        @Override
//        public void AddCard(List<AbstractCard> list, AbstractCard card, int index)
//        {
//            list.add(Math.max(0, Math.min(list.size(), index + shift)), card);
//        }
//
//        @Override
//        public AbstractCard GetCard(List<AbstractCard> list, int index, boolean remove)
//        {
//            AbstractCard card = null;
//            int position = index + shift;
//            if (position >= 0 && position < list.size())
//            {
//                card = list.get(position);
//                if (remove)
//                {
//                    list.remove(position);
//                }
//            }
//
//            return card;
//        }
//    }
//
//    private static class Top extends CardSelection
//    {
//        private final int shift;
//
//        public Top(int shift)
//        {
//            super(Mode.Top);
//
//            this.shift = shift;
//        }
//
//        @Override
//        public void AddCard(List<AbstractCard> list, AbstractCard card, int index)
//        {
//            list.add(Math.max(0, Math.min(list.size(), list.size() - index - shift)), card);
//        }
//
//        @Override
//        public AbstractCard GetCard(List<AbstractCard> list, int index, boolean remove)
//        {
//            AbstractCard card = null;
//            int position = list.size() - 1 - index - shift;
//            if (position >= 0 && position < list.size())
//            {
//                card = list.get(position);
//                if (remove)
//                {
//                    list.remove(position);
//                }
//            }
//
//            return card;
//        }
//    }
//
//    private static class Random extends CardSelection
//    {
//        private final com.megacrit.cardcrawl.random.Random rng;
//
//        private Random(com.megacrit.cardcrawl.random.Random rng)
//        {
//            super(Mode.Random);
//
//            this.rng = rng;
//        }
//
//        protected int GetRandomIndex(int size)
//        {
//            if (size <= 0)
//            {
//                return 0;
//            }
//            else if (rng == null)
//            {
//                return GameUtilities.GetRNG().random(size);
//            }
//            else
//            {
//                return rng.random(size);
//            }
//        }
//
//        @Override
//        public void AddCard(List<AbstractCard> list, AbstractCard card, int index)
//        {
//            list.add(GetRandomIndex(list.size() - 1), card);
//        }
//
//        @Override
//        public AbstractCard GetCard(List<AbstractCard> list, int index, boolean remove)
//        {
//            AbstractCard card = null;
//            if (list.size() > 0)
//            {
//                int position = GetRandomIndex(list.size() - 1);
//                card = list.get(position);
//                if (remove)
//                {
//                    list.remove(position);
//                }
//            }
//
//            return card;
//        }
//    }
//
//    private static class Special extends CardSelection
//    {
//        private ActionT3<List<AbstractCard>, AbstractCard, Integer> addCard;
//        private FuncT3<AbstractCard, List<AbstractCard>, Integer, Boolean> getCard;
//
//        public Special(ActionT3<List<AbstractCard>, AbstractCard, Integer> addCard,
//                FuncT3<AbstractCard, List<AbstractCard>, Integer, Boolean> getCard)
//        {
//            super(Mode.Special);
//
//            this.addCard = addCard;
//            this.getCard = getCard;
//        }
//
//        @Override
//        public void AddCard(List<AbstractCard> list, AbstractCard card, int index)
//        {
//            addCard.Invoke(list, card, index);
//        }
//
//        @Override
//        public AbstractCard GetCard(List<AbstractCard> list, int index, boolean remove)
//        {
//            return getCard.Invoke(list, index, remove);
//        }
//    }
//}

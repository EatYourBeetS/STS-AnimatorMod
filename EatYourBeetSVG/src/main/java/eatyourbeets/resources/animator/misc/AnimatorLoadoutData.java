package eatyourbeets.resources.animator.misc;

import java.util.ArrayList;
import java.util.Iterator;

public class AnimatorLoadoutData implements Iterable<AnimatorCardSlot>
{
    public int Preset;
    public int Gold;
    public int HP;

    protected final ArrayList<AnimatorCardSlot> cardSlots = new ArrayList<>();

    protected AnimatorLoadoutData()
    {

    }
    
    public AnimatorLoadoutData(AnimatorLoadout loadout)
    {
        loadout.InitializeData(this);
    }

    public AnimatorCardSlot AddCardSlot(int min, int max)
    {
        final AnimatorCardSlot slot = new AnimatorCardSlot(this, min, max);
        cardSlots.add(slot);

        return slot;
    }

    public AnimatorCardSlot GetCardSlot(int index)
    {
        return cardSlots.get(index);
    }

    public int Size()
    {
        return cardSlots.size();
    }

    public AnimatorLoadoutData MakeCopy()
    {
        return MakeCopy(Preset);
    }

    public AnimatorLoadoutData MakeCopy(int preset)
    {
        final AnimatorLoadoutData copy = new AnimatorLoadoutData();
        copy.Preset = preset;
        copy.Gold = Gold;
        copy.HP = HP;
        for (AnimatorCardSlot slot : cardSlots)
        {
            copy.cardSlots.add(slot.MakeCopy(copy));
        }

        return copy;
    }

    public AnimatorLoadout.Validation Validate()
    {
        return AnimatorLoadout.Validation.For(this);
    }

    @Override
    public Iterator<AnimatorCardSlot> iterator()
    {
        return cardSlots.iterator();
    }
}

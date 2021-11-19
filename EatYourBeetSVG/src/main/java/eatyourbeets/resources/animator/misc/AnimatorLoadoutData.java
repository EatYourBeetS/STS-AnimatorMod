package eatyourbeets.resources.animator.misc;

import java.util.ArrayList;

public class AnimatorLoadoutData
{
    public int Preset;
    public int Gold;
    public int HP;
    public final ArrayList<AnimatorCardSlot> cardSlots = new ArrayList<>();
    public final ArrayList<AnimatorRelicSlot> relicSlots = new ArrayList<>();

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

    public AnimatorRelicSlot AddRelicSlot()
    {
        final AnimatorRelicSlot slot = new AnimatorRelicSlot(this);
        relicSlots.add(slot);

        return slot;
    }

    public AnimatorCardSlot GetCardSlot(int index)
    {
        return cardSlots.get(index);
    }

    public AnimatorRelicSlot GetRelicSlot(int index)
    {
        return relicSlots.get(index);
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
        for (AnimatorRelicSlot slot : relicSlots)
        {
            copy.relicSlots.add(slot.MakeCopy(copy));
        }

        return copy;
    }

    public AnimatorLoadout.Validation Validate()
    {
        return AnimatorLoadout.Validation.For(this);
    }
}

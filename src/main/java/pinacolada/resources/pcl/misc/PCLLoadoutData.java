package pinacolada.resources.pcl.misc;

import pinacolada.ui.characterSelection.PCLBaseStatEditor;

import java.util.ArrayList;
import java.util.HashMap;

public class PCLLoadoutData
{
    public int Preset;
    public final HashMap<PCLBaseStatEditor.StatType, Integer> Values = new HashMap<>();
    public final ArrayList<PCLCardSlot> cardSlots = new ArrayList<>();
    public final ArrayList<PCLRelicSlot> relicSlots = new ArrayList<>();

    protected PCLLoadoutData()
    {

    }
    
    public PCLLoadoutData(PCLLoadout loadout)
    {
        loadout.InitializeData(this);
    }

    public PCLCardSlot AddCardSlot() {
        return AddCardSlot(0, PCLCardSlot.MAX_LIMIT);
    }

    public PCLCardSlot AddCardSlot(int min, int max)
    {
        final PCLCardSlot slot = new PCLCardSlot(this, min, max);
        cardSlots.add(slot);

        return slot;
    }

    public PCLRelicSlot AddRelicSlot()
    {
        final PCLRelicSlot slot = new PCLRelicSlot(this);
        relicSlots.add(slot);

        return slot;
    }

    public PCLCardSlot GetCardSlot(int index)
    {
        return cardSlots.get(index);
    }

    public PCLRelicSlot GetRelicSlot(int index)
    {
        return relicSlots.get(index);
    }

    public int CardsSize()
    {
        return cardSlots.size();
    }

    public int RelicsSize()
    {
        return relicSlots.size();
    }

    public PCLLoadoutData MakeCopy()
    {
        return MakeCopy(Preset);
    }

    public PCLLoadoutData MakeCopy(int preset)
    {
        final PCLLoadoutData copy = new PCLLoadoutData();
        copy.Preset = preset;
        copy.Values.putAll(Values);
        for (PCLCardSlot slot : cardSlots)
        {
            copy.cardSlots.add(slot.MakeCopy(copy));
        }
        for (PCLRelicSlot slot : relicSlots)
        {
            copy.relicSlots.add(slot.MakeCopy(copy));
        }

        return copy;
    }

    public PCLLoadout.Validation Validate()
    {
        return PCLLoadout.Validation.For(this);
    }
}

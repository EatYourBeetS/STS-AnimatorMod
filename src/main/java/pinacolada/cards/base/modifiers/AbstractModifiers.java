package pinacolada.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;

public abstract class AbstractModifiers
{
    protected int baseAmount;
    protected int previousAmount;
    protected final AbstractCard card;
    protected final HashMap<String, Integer> modifiers = new HashMap<>();

    public AbstractModifiers(AbstractCard card)
    {
        this.card = card;
    }

    public void CopyFrom(AbstractModifiers other, boolean copyAll)
    {
        this.baseAmount = other.baseAmount;
        this.previousAmount = other.previousAmount;

        if (copyAll)
        {
            for (String key : other.modifiers.keySet())
            {
                this.modifiers.put(key, other.modifiers.get(key));
            }
        }
    }

    public void Add(int amount)
    {
        baseAmount += amount;
        Apply(card);
    }

    public void Add(String key, int amount)
    {
        PCLJUtils.IncrementMapElement(modifiers, key, amount);
        Apply(card);
    }

    public void Set(int amount)
    {
        baseAmount = amount;
        Apply(card);
    }

    public void Set(String key, int amount)
    {
        modifiers.put(key, amount);
        Apply(card);
    }

    public void Remove(String key)
    {
        if (modifiers.remove(key) != null)
        {
            Apply(card);
        }
    }

    public int Get(String key)
    {
        return modifiers.get(key);
    }

    protected abstract void Apply(AbstractCard card);
}
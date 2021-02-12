package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;

public abstract class AbstractModifiers
{
    public int baseAmount;
    public int previousAmount;

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
        JUtils.IncrementMapElement(modifiers, key, amount);
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

    public void Remove(String key, boolean applyInstantly)
    {
        if (modifiers.remove(key) != null && applyInstantly)
        {
            Apply(card);
        }
    }

    public void Remove(String key)
    {
        Remove(key, true);
    }

    public int Get(String key)
    {
        return modifiers.get(key);
    }

    protected abstract void Apply(AbstractCard card);
}
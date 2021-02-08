package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;

public abstract class AbstractModifier
{
    public int baseAmount;
    public int previousAmount;

    protected final AbstractCard card;
    protected final HashMap<String, Integer> modifiers = new HashMap<>();

    public AbstractModifier(AbstractCard card)
    {
        this.card = card;
    }

    public void CopyFrom(AbstractModifier other, boolean copyAll)
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

    public void AddModifier(int amount)
    {
        baseAmount += amount;
        Apply(card);
    }

    public void AddModifier(String key, int amount)
    {
        JUtils.IncrementMapElement(modifiers, key, amount);
        Apply(card);
    }

    public void SetModifier(int amount)
    {
        baseAmount = amount;
        Apply(card);
    }

    public void SetModifier(String key, int amount)
    {
        modifiers.put(key, amount);
        Apply(card);
    }

    public void RemoveModifier(String key)
    {
        modifiers.remove(key);
        Apply(card);
    }

    public int GetModifier(String key)
    {
        return modifiers.get(key);
    }

    protected abstract void Apply(AbstractCard card);
}
package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnCardResetSubscriber;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import patches.abstractCard.AbstractCard_Fields;

import java.util.HashMap;

public class CostModifier implements OnCardResetSubscriber
{
    public static CostModifier Initialize(AbstractCard card)
    {
        CostModifier modifier = AbstractCard_Fields.costModifier.get(card);
        if (modifier == null)
        {
            modifier = new CostModifier(card);
            AbstractCard_Fields.costModifier.set(card, modifier);
        }

        return modifier;
    }

    public int baseAmount;

    protected final AbstractCard card;
    protected final HashMap<String, Integer> modifiers = new HashMap<>();
    protected int previousAmount;

    public CostModifier(AbstractCard card)
    {
        this.card = card;
        this.baseAmount = 0;
    }

    public void IncreaseModifier(String key, int amount)
    {
        JUtils.IncrementMapElement(modifiers, key, amount);
        ModifyCost(card);
    }

    public void SetModifier(int amount)
    {
        baseAmount = amount;
        ModifyCost(card);
    }

    public void SetModifier(String key, int amount)
    {
        modifiers.put(key, amount);
        ModifyCost(card);
    }

    public int GetModifier(String key)
    {
        return modifiers.get(key);
    }

    @Override
    public void OnCardReset(AbstractCard card)
    {
        previousAmount = 0;
        ModifyCost(card);
    }

    private void ModifyCost(AbstractCard card)
    {
        int currentCost = (card.costForTurn - previousAmount);
        int modifier = baseAmount;
        for (Integer n : modifiers.values())
        {
            modifier += n;
        }

        previousAmount = modifier;

        GameUtilities.ModifyCostForTurn(card, currentCost + modifier, false);
    }
}
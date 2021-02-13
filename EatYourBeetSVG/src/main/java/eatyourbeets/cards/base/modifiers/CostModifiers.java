package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnCardResetSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCard_Fields;

public class CostModifiers extends AbstractModifiers implements OnCardResetSubscriber
{
    public static CostModifiers For(AbstractCard card)
    {
        CostModifiers modifier = AbstractCard_Fields.costModifiers.get(card);
        if (modifier == null)
        {
            modifier = new CostModifiers(card);
            AbstractCard_Fields.costModifiers.set(card, modifier);
        }

        return modifier;
    }

    public CostModifiers(AbstractCard card)
    {
        super(card);
    }

    @Override
    public void OnCardReset(AbstractCard card)
    {
        if (this.card == card)
        {
            previousAmount = 0;
            Apply(card);
        }
    }

    public void Remove(String key, boolean applyInstantly)
    {
        if (modifiers.remove(key) != null && applyInstantly)
        {
            Apply(card);
        }
    }

    @Override
    public void Remove(String key)
    {
        Remove(key, true);
    }

    @Override
    protected void Apply(AbstractCard card)
    {
        CombatStats.onCardReset.Subscribe(this);

        if (card.freeToPlay()) //|| !AbstractDungeon.player.hand.contains(card))
        {
            previousAmount = 0;
            return;
        }

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
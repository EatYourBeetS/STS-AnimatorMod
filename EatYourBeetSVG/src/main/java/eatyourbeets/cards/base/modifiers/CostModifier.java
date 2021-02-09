package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnCardResetSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCard_Fields;

public class CostModifier extends AbstractModifier implements OnCardResetSubscriber
{
    public static CostModifier For(AbstractCard card)
    {
        CostModifier modifier = AbstractCard_Fields.costModifier.get(card);
        if (modifier == null)
        {
            modifier = new CostModifier(card);
            AbstractCard_Fields.costModifier.set(card, modifier);
            CombatStats.onCardReset.Subscribe(modifier);
        }

        return modifier;
    }

    public CostModifier(AbstractCard card)
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

    @Override
    protected void Apply(AbstractCard card)
    {
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
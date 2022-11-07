package eatyourbeets.cards.base.modifiers;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.subscribers.OnCardResetSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCardPatches;

public class CostModifiers extends AbstractModifiers implements OnCardResetSubscriber
{
    public static SpireField<CostModifiers> Field = AbstractCardPatches.AbstractCard_Fields.costModifiers;

    public static CostModifiers For(AbstractCard card)
    {
        CostModifiers modifier = Field.get(card);
        if (modifier == null)
        {
            modifier = new CostModifiers(card);
            Field.set(card, modifier);
        }

        return modifier;
    }

    public CostModifiers(AbstractCard card)
    {
        super(card);
    }

    @Override
    public void CopyFrom(AbstractModifiers other, boolean copyAll)
    {
        if (baseAmount != other.baseAmount || previousAmount != other.previousAmount || (other.modifiers.size() > 0 && copyAll))
        {
            CombatStats.onCardReset.Subscribe(this);
        }

        super.CopyFrom(other, copyAll);
    }

    @Override
    public void OnCardReset(AbstractCard card)
    {
        if (this.card == card)
        {
            previousAmount = 0;
            Apply();
        }
    }

    public void Remove(String key, boolean applyInstantly)
    {
        if (modifiers.remove(key) != null && applyInstantly)
        {
            Apply();
        }
    }

    @Override
    public void Remove(String key)
    {
        Remove(key, true);
    }

    @Override
    protected void Apply()
    {
        CombatStats.onCardReset.Subscribe(this);

        if (card.freeToPlay()) //|| !AbstractDungeon.player.hand.contains(card))
        {
            previousAmount = 0;
            return;
        }

        final int currentCost = (card.costForTurn - previousAmount);
        int modifier = baseAmount;
        for (Integer n : modifiers.values())
        {
            modifier += n;
        }

        previousAmount = modifier;

        final int newCost = currentCost + modifier;
        GameUtilities.ModifyCostForTurn(card, Math.max(0, newCost), false);

        if (newCost < 0)
        {
            previousAmount -= newCost;
        }
    }
}
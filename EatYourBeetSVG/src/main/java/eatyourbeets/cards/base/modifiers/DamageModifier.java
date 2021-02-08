package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCard_Fields;

public class DamageModifier extends AbstractModifier
{
    public static DamageModifier For(AbstractCard card)
    {
        DamageModifier modifier = AbstractCard_Fields.damageModifier.get(card);
        if (modifier == null)
        {
            modifier = new DamageModifier(card);
            AbstractCard_Fields.damageModifier.set(card, modifier);
        }

        return modifier;
    }

    public DamageModifier(AbstractCard card)
    {
        super(card);
    }

    @Override
    protected void Apply(AbstractCard card)
    {
        int currentAmount = (card.baseDamage - previousAmount);
        int modifier = baseAmount;
        for (Integer n : modifiers.values())
        {
            modifier += n;
        }

        previousAmount = modifier;

        GameUtilities.ModifyDamage(card, currentAmount + modifier, false);
    }
}
package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCard_Fields;

public class DamageModifiers extends AbstractModifiers
{
    public static DamageModifiers For(AbstractCard card)
    {
        DamageModifiers modifier = AbstractCard_Fields.damageModifiers.get(card);
        if (modifier == null)
        {
            modifier = new DamageModifiers(card);
            AbstractCard_Fields.damageModifiers.set(card, modifier);
        }

        return modifier;
    }

    public DamageModifiers(AbstractCard card)
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
package eatyourbeets.cards.base.modifiers;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCardPatches;

public class DamageModifiers extends AbstractModifiers
{
    public static SpireField<DamageModifiers> Field = AbstractCardPatches.AbstractCard_Fields.damageModifiers;

    public static DamageModifiers For(AbstractCard card)
    {
        DamageModifiers modifier = Field.get(card);
        if (modifier == null)
        {
            modifier = new DamageModifiers(card);
            Field.set(card, modifier);
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
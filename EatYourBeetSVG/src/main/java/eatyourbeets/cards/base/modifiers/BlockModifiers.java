package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCard_Fields;

public class BlockModifiers extends AbstractModifiers
{
    public static BlockModifiers For(AbstractCard card)
    {
        BlockModifiers modifier = AbstractCard_Fields.blockModifiers.get(card);
        if (modifier == null)
        {
            modifier = new BlockModifiers(card);
            AbstractCard_Fields.blockModifiers.set(card, modifier);
        }

        return modifier;
    }

    public BlockModifiers(AbstractCard card)
    {
        super(card);
    }

    @Override
    protected void Apply(AbstractCard card)
    {
        int currentAmount = (card.baseBlock - previousAmount);
        int modifier = baseAmount;
        for (Integer n : modifiers.values())
        {
            modifier += n;
        }

        previousAmount = modifier;

        GameUtilities.ModifyBlock(card, currentAmount + modifier, false);
    }
}
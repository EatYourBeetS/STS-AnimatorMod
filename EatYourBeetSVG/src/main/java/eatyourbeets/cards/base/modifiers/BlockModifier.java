package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCard_Fields;

public class BlockModifier extends AbstractModifier
{
    public static BlockModifier For(AbstractCard card)
    {
        BlockModifier modifier = AbstractCard_Fields.blockModifier.get(card);
        if (modifier == null)
        {
            modifier = new BlockModifier(card);
            AbstractCard_Fields.blockModifier.set(card, modifier);
        }

        return modifier;
    }

    public BlockModifier(AbstractCard card)
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
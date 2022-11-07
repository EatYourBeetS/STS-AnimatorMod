package eatyourbeets.cards.base.modifiers;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import patches.abstractCard.AbstractCardPatches;

public class BlockModifiers extends AbstractModifiers
{
    public static SpireField<BlockModifiers> Field = AbstractCardPatches.AbstractCard_Fields.blockModifiers;

    public static BlockModifiers For(AbstractCard card)
    {
        BlockModifiers modifier = Field.get(card);
        if (modifier == null)
        {
            modifier = new BlockModifiers(card);
            Field.set(card, modifier);
        }

        return modifier;
    }

    public BlockModifiers(AbstractCard card)
    {
        super(card);
    }

    @Override
    protected void Apply()
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
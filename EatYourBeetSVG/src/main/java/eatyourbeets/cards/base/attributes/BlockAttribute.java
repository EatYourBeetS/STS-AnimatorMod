package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;

public class BlockAttribute extends AbstractAttribute
{
    public static final BlockAttribute Instance = new BlockAttribute();

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        icon = ICONS.Block.Texture();
        iconTag = null;
        suffix = null;
        mainText = card.GetBlockString();

        return this;
    }
}

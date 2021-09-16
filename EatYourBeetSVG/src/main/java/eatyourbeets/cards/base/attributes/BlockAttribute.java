package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;

public class BlockAttribute extends AbstractAttribute
{
    public static final BlockAttribute Instance = new BlockAttribute();

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        icon = card.cardData.BlockScalingAttack ? ICONS.BlockScaling.Texture() : ICONS.Block.Texture();
        largeIcon = card.cardData.BlockScalingAttack ? ICONS.BlockScaling_L.Texture() : ICONS.Block_L.Texture();
        iconTag = null;
        suffix = null;
        mainText = card.GetBlockString();

        return this;
    }
}

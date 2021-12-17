package pinacolada.cards.base.attributes;

import pinacolada.cards.base.PCLCard;

public class BlockAttribute extends AbstractAttribute
{
    public static final BlockAttribute Instance = new BlockAttribute();

    @Override
    public AbstractAttribute SetCard(PCLCard card)
    {
        icon = card.cardData.BlockScalingAttack ? ICONS.BlockScaling.Texture() : ICONS.Block.Texture();
        largeIcon = card.cardData.BlockScalingAttack ? ICONS.BlockScaling_L.Texture() : ICONS.Block_L.Texture();
        iconTag = null;
        suffix = null;
        mainText = card.GetBlockString();

        return this;
    }
}

package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;

public class SpecialAttribute extends AbstractAttribute
{
    public static SpecialAttribute Instance = new SpecialAttribute();

    public SpecialAttribute()
    {
        this.icon = ICONS.Special1.Texture();
    }

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        mainText = null;
        iconTag = null;
        suffix = null;
        icon = ICONS.Special1.Texture();

        return this;
    }

    public AbstractAttribute SetCard(EYBCard card, EYBCardTooltip tip, int amount)
    {
        mainText = new ColoredString(amount, Colors.Cream(1));
        iconTag = null;
        suffix = null;
        icon = tip != null ? tip.icon.getTexture() : ICONS.Special1.Texture();

        return this;
    }

    public AbstractAttribute SetCard(EYBCard card, EYBCardTooltip tip, boolean useMagicNumber)
    {
        mainText = useMagicNumber ? card.GetMagicNumberString() : null;
        iconTag = null;
        suffix = null;
        icon = tip != null ? tip.icon.getTexture() : ICONS.Special1.Texture();

        return this;
    }
}

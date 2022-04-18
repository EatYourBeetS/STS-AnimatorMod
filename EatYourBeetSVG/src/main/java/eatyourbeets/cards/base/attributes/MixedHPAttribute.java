package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;

public class MixedHPAttribute extends AbstractAttribute
{
    public static MixedHPAttribute Instance = new MixedHPAttribute();

    public MixedHPAttribute()
    {
        this.icon = ICONS.MultiHP.Texture();
    }

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        mainText = null;
        iconTag = null;
        suffix = null;

        return this;
    }

    public AbstractAttribute SetCard(EYBCard card, int amount)
    {
        mainText = new ColoredString(amount, Colors.Cream(1));
        iconTag = null;
        suffix = null;

        return this;
    }

    public AbstractAttribute SetCard(EYBCard card, boolean useMagicNumber)
    {
        if (useMagicNumber)
        {
            mainText = card.GetMagicNumberString();
        }
        else
        {
            mainText = null;
        }

        iconTag = null;
        suffix = null;

        return this;
    }
}

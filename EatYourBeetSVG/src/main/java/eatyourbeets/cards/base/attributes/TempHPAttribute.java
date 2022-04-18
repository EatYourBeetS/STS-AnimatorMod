package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;

public class TempHPAttribute extends AbstractAttribute
{
    public static TempHPAttribute Instance = new TempHPAttribute();

    public TempHPAttribute()
    {
        this.icon = ICONS.TempHP.Texture();
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

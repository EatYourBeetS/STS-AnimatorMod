package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;

public class HPAttribute extends AbstractAttribute
{
    public static HPAttribute Instance = new HPAttribute();

    public HPAttribute()
    {
        this.icon = ICONS.HP.Texture();
    }

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        mainText = null;
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

package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;

public class TempHPAttribute extends AbstractAttribute
{
    public static TempHPAttribute Instance = new TempHPAttribute();

    public TempHPAttribute()
    {
        this.icon = GR.Common.Images.TempHP.Texture();
    }

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        mainText = null;
        iconTag = null;
        suffix = null;

        return this;
    }

    public AbstractAttribute SetCard(EYBCard card, ColoredString text)
    {
        mainText = text;
        iconTag = null;
        suffix = null;

        return this;
    }
}

package pinacolada.cards.base.attributes;

import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.PCLCard;

public class HPAttribute extends AbstractAttribute
{
    public static HPAttribute Instance = new HPAttribute();

    public HPAttribute()
    {
        this.icon = ICONS.HP.Texture();
        this.largeIcon = ICONS.HP_L.Texture();
    }

    @Override
    public AbstractAttribute SetCard(PCLCard card)
    {
        mainText = card.heal > 0 ? new ColoredString(card.heal, Colors.Cream(1.0F)) : null;
        iconTag = null;
        suffix = null;

        return this;
    }

    public AbstractAttribute SetCard(PCLCard card, boolean useMagicNumber)
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

package pinacolada.cards.base.attributes;

import pinacolada.cards.base.PCLCard;

public class TempHPAttribute extends AbstractAttribute
{
    public static TempHPAttribute Instance = new TempHPAttribute();

    public TempHPAttribute()
    {
        this.icon = ICONS.TempHP.Texture();
        this.largeIcon = ICONS.TempHP_L.Texture();
    }

    @Override
    public AbstractAttribute SetCard(PCLCard card)
    {
        mainText = null;
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

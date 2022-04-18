package eatyourbeets.cards.effects.GenericEffects;

import eatyourbeets.cards.base.EYBCard;

public abstract class GenericEffect_Auto extends GenericEffect
{
    protected final EYBCard source;
    protected final String description;

    public GenericEffect_Auto(EYBCard source, int descriptionIndex)
    {
        this.source = source;
        this.description = source.cardData.Strings.EXTENDED_DESCRIPTION[descriptionIndex];
    }

    @Override
    public String GetText()
    {
        return description;
    }
}
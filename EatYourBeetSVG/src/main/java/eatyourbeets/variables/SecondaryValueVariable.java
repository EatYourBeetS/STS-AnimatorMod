package eatyourbeets.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.EYBCard;
import eatyourbeets.utilities.Utilities;

public class SecondaryValueVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "SV";
    }

    public boolean isModified(AbstractCard card)
    {
        EYBCard c = Utilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.isSecondaryValueModified;
        }

        return false;
    }

    public void setIsModified(AbstractCard card, boolean v)
    {
        EYBCard c = Utilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            c.isSecondaryValueModified = v;
        }
    }

    public int value(AbstractCard card)
    {
        EYBCard c = Utilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.secondaryValue;
        }

        return 0;
    }

    public int baseValue(AbstractCard card)
    {
        EYBCard c = Utilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.baseSecondaryValue;
        }

        return 0;
    }

    public boolean upgraded(AbstractCard card)
    {
        EYBCard c = Utilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.upgradedSecondaryValue;
        }

        return false;
    }
}

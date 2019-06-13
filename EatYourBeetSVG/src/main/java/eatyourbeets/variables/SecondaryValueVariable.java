package eatyourbeets.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class SecondaryValueVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "SV";
    }

    public boolean isModified(AbstractCard card)
    {
        AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
        if (c != null)
        {
            return c.isSecondaryValueModified;
        }

        return false;
    }

    public void setIsModified(AbstractCard card, boolean v)
    {
        AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
        if (c != null)
        {
            c.isSecondaryValueModified = v;
        }
    }

    public int value(AbstractCard card)
    {
        AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
        if (c != null)
        {
            return c.secondaryValue;
        }

        return 0;
    }

    public int baseValue(AbstractCard card)
    {
        AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
        if (c != null)
        {
            return c.baseSecondaryValue;
        }

        return 0;
    }

    public boolean upgraded(AbstractCard card)
    {
        AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
        if (c != null)
        {
            return c.upgradedSecondaryValue;
        }

        return false;
    }
}

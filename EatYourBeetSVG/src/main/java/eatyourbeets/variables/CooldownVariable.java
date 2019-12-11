package eatyourbeets.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.JavaUtilities;

public class CooldownVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "CD";
    }

    public boolean isModified(AbstractCard card)
    {
        EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.isSecondaryValueModified;
        }

        return false;
    }

    public void setIsModified(AbstractCard card, boolean v)
    {
        EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            c.isSecondaryValueModified = v;
        }
    }

    public int value(AbstractCard card)
    {
        EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.secondaryValue;
        }

        return 0;
    }

    public int baseValue(AbstractCard card)
    {
        EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.baseSecondaryValue;
        }

        return 0;
    }

    public boolean upgraded(AbstractCard card)
    {
        EYBCard c = JavaUtilities.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            return c.upgradedSecondaryValue;
        }

        return false;
    }

    public Color getNormalColor() {
        return Settings.CREAM_COLOR;
    }

    public Color getUpgradedColor() {
        return Settings.GREEN_TEXT_COLOR;
    }

    public Color getIncreasedValueColor()
    {
        return Settings.RED_TEXT_COLOR;
    }

    public Color getDecreasedValueColor()
    {
        return Settings.GREEN_TEXT_COLOR;
    }
}

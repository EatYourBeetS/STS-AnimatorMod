package eatyourbeets.events.base;

import com.megacrit.cardcrawl.localization.EventStrings;
import eatyourbeets.utilities.JavaUtilities;

public abstract class EYBEventStrings
{
    protected String name;
    protected String[] options;
    protected String[] descriptions;

    public EYBEventStrings SetStrings(EventStrings strings)
    {
        name = strings.NAME;
        options = strings.OPTIONS;
        descriptions = strings.DESCRIPTIONS;

        return this;
    }

    public final String GetDescription(int index)
    {
        return descriptions[index];
    }

    public final String GetDescription(int index, Object... args)
    {
        return JavaUtilities.Format(descriptions[index], args);
    }

    public final String GetOption(int index)
    {
        return options[index];
    }

    public final String GetOption(int index, Object... args)
    {
        return JavaUtilities.Format(options[index], args);
    }
}

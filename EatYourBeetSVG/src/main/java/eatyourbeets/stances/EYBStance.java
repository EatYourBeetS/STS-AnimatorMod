package eatyourbeets.stances;

import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.resources.GR;

public abstract class EYBStance extends AbstractStance
{
    public final StanceStrings Strings;
    public final String Name;
    public final String[] Description;

    public static String CreateFullID(Class<? extends EYBStance> type)
    {
        return GR.Common.CreateID(type.getSimpleName());
    }

    protected EYBStance(String id)
    {
        Strings = GR.GetStanceString(id);
        Name = Strings.NAME;
        Description = Strings.DESCRIPTION;
    }

    @Override
    public void updateDescription()
    {
        description = Description[0];
    }

    //TODO: Create Patch
    public static AbstractStance GetStanceFromName(String name)
    {
        if (name.equals("Custom Stance Name Here"))
        {
            return new NeutralStance();
        }

        return null;
    }
}

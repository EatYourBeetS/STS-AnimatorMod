package eatyourbeets.blights;


import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.localization.BlightStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class AnimatorBlight extends AbstractBlight
{
    protected final BlightStrings strings;
    protected final int initialAmount;

    public static String CreateFullID(Class<? extends AnimatorBlight> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public AnimatorBlight(String id)
    {
        this(id, GR.GetBlightStrings(id), -1);
    }

    public AnimatorBlight(String id, int amount)
    {
        this(id, GR.GetBlightStrings(id), amount);
    }

    public AnimatorBlight(String id, BlightStrings strings, int amount)
    {
        super(id, strings.NAME, JUtils.Format(strings.DESCRIPTION[0], amount), GR.GetBlightImageName(id), true);

        this.initialAmount = amount;
        this.counter = amount;
        this.strings = strings;
    }
}

package eatyourbeets.blights;


import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.localization.BlightStrings;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.utilities.JavaUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnimatorBlight extends AbstractBlight
{
    protected static final Logger logger = LogManager.getLogger(AnimatorRelic.class.getName());

    protected BlightStrings strings;
    protected int initialAmount;

    public static String CreateFullID(String id)
    {
        return AnimatorResources.CreateID(id);
    }

    public AnimatorBlight(String id)
    {
        this(id, AbstractResources.GetBlightStrings(id), -1);
    }

    public AnimatorBlight(String id, int amount)
    {
        this(id, AbstractResources.GetBlightStrings(id), amount);
    }

    public AnimatorBlight(String id, BlightStrings strings, int amount)
    {
        super(id, strings.NAME, JavaUtilities.Format(strings.DESCRIPTION[0], amount), AbstractResources.GetBlightImageName(id), true);

        this.initialAmount = amount;
        this.counter = amount;
        this.strings = strings;
    }
}

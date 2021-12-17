package pinacolada.blights;


import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.localization.BlightStrings;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

public class PCLBlight extends AbstractBlight
{
    protected final BlightStrings strings;
    protected final int initialAmount;

    public static String CreateFullID(Class<? extends PCLBlight> type)
    {
        return GR.PCL.CreateID(type.getSimpleName());
    }

    public PCLBlight(String id)
    {
        this(id, GR.GetBlightStrings(id), -1);
    }

    public PCLBlight(String id, int amount)
    {
        this(id, GR.GetBlightStrings(id), amount);
    }

    public PCLBlight(String id, BlightStrings strings, int amount)
    {
        super(id, strings.NAME, PCLJUtils.Format(strings.DESCRIPTION[0], amount), "durian.png", true);

        this.img = GR.GetTexture(GR.GetBlightImage(id));
        this.outlineImg = GR.GetTexture(GR.GetBlightOutlineImage(id));
        this.initialAmount = amount;
        this.counter = amount;
        this.strings = strings;
    }
}

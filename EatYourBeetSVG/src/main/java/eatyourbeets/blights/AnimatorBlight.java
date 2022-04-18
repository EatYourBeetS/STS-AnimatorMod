package eatyourbeets.blights;


import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.localization.BlightStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class AnimatorBlight extends AbstractBlight
{
    protected final BlightStrings strings;
    protected int initialAmount;
    protected boolean unnamedReign;

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
        super(id, strings.NAME, JUtils.Format(strings.DESCRIPTION[0], amount), "durian.png", true);

        this.img = GR.GetTexture(GR.GetBlightImage(id));
        this.outlineImg = GR.GetTexture(GR.GetBlightOutlineImage(id));
        this.initialAmount = amount;
        this.counter = amount;
        this.strings = strings;
    }

    @Override
    public void updateDescription()
    {
        this.tips.get(0).body = this.description = GetUpdatedDescription();
    }

    public String GetUpdatedDescription()
    {
        return description;
    }

    protected String FormatDescription(int index, Object... args)
    {
        return JUtils.Format(strings.DESCRIPTION[index], args);
    }

    protected static boolean IsUnnamedReign()
    {
        return !GameUtilities.InGame() || GR.Common.Dungeon.IsUnnamedReign();
    }
}

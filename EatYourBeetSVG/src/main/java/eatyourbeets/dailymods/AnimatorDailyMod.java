package eatyourbeets.dailymods;

import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.localization.RunModStrings;
import eatyourbeets.resources.GR;

import java.util.Arrays;
import java.util.List;

public class AnimatorDailyMod extends AbstractDailyMod {

    public enum ModColor { BLUE, GREEN, RED }

    private static final String TESTIMAGE = "draft.png";
    public static final List<AnimatorDailyMod> mods = Arrays.asList(
            //blue
            new SeriesDeck(),

            //green
            new AllRelicAnimatorRun(),

            //red
            new NoRelics()
    );
    private ModColor color;

    public boolean noDisplaySeries;

    public AnimatorDailyMod(String modName, ModColor color) {
        this(modName, color, false);
    }

    public AnimatorDailyMod(String modName, ModColor color, boolean noDisplaySeries) {
        super(modName, "", "", TESTIMAGE, color == ModColor.RED);

        RunModStrings modStrings = GR.GetRunModStrings(modName);
        this.name = modStrings.NAME;
        this.description = modStrings.DESCRIPTION;
        this.noDisplaySeries = noDisplaySeries;
        this.color = color;
    }

    public AnimatorDailyMod clone()
    {
        return new AnimatorDailyMod(name, color, noDisplaySeries);
    }

    public String getColor()
    {
        if (color == ModColor.BLUE)
        {
            return "b";
        }
        else if (color == ModColor.GREEN)
        {
            return "g";
        }
        else if (color == ModColor.RED)
        {
            return "r";
        }

        return null;
    }
}

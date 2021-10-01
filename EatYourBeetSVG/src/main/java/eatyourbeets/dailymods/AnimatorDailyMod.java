package eatyourbeets.dailymods;

import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.localization.RunModStrings;
import eatyourbeets.resources.GR;

import java.util.Arrays;
import java.util.List;

public class AnimatorDailyMod extends AbstractDailyMod {

    private static final String TESTIMAGE = "draft.png";
    public static final List<AnimatorDailyMod> mods = Arrays.asList(new SeriesDeck());

    public boolean noDisplaySeries;

    public AnimatorDailyMod(String modName, boolean positive) {
        this(modName, positive, false);
    }

    public AnimatorDailyMod(String modName, boolean positive, boolean noDisplaySeries) {
        super(modName, "", "", TESTIMAGE, positive);

        RunModStrings modStrings = GR.GetRunModStrings(modName);
        this.name = modStrings.NAME;
        this.description = modStrings.DESCRIPTION;
        this.noDisplaySeries = noDisplaySeries;
    }

    public AnimatorDailyMod clone()
    {
        return new AnimatorDailyMod(name, positive);
    }
}

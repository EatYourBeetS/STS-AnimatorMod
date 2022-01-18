package pinacolada.dailymods;

import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.localization.RunModStrings;
import eatyourbeets.interfaces.delegates.FuncT0;
import pinacolada.resources.GR;

import java.util.Arrays;
import java.util.List;

public class PCLDailyMod extends AbstractDailyMod {

    public enum ModColor { BLUE, GREEN, RED }

    private static final String TESTIMAGE = "draft.png";
    public static final List<FuncT0<PCLDailyMod>> mods = Arrays.asList(
            SeriesDeck::new, AllRelicPCLRun::new, NoRelics::new
    );
    private ModColor color;

    public boolean noDisplaySeries;

    public PCLDailyMod(String modName, ModColor color) {
        this(modName, color, false);
    }

    public PCLDailyMod(String modName, ModColor color, boolean noDisplaySeries) {
        super(modName, "", "", TESTIMAGE, color == ModColor.RED);

        RunModStrings modStrings = GR.GetRunModStrings(modName);
        this.name = modStrings.NAME;
        this.description = modStrings.DESCRIPTION;
        this.noDisplaySeries = noDisplaySeries;
        this.color = color;
    }

    public PCLDailyMod clone()
    {
        return new PCLDailyMod(name, color, noDisplaySeries);
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

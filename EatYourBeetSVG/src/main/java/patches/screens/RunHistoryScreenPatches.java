package patches.screens;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

public class RunHistoryScreenPatches
{
    private static final FieldInfo<RunData> _viewedRun = JUtils.GetField("viewedRun", RunHistoryScreen.class);

    @SpirePatch(clz = RunHistoryScreen.class, method = "characterText", paramtypez = {String.class})
    public static class RunHistoryScreen_characterText
    {
        @SpirePostfixPatch
        public static String Postfix(String result, RunHistoryScreen __instance, String chosenCharacter)
        {
            final RunData data = _viewedRun.Get(__instance);
            if (data != null && StringUtils.isNotEmpty(data.loadout)) // See MetricsPatches
            {
                return data.loadout;
            }

            return result;
        }
    }
}

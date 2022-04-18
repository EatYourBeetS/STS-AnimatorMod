package patches.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;

public class RunHistoryScreenPatches
{
    private static final FieldInfo<RunData> _viewedRun = JUtils.GetField("viewedRun", RunHistoryScreen.class);
    private static final ArrayList<RunData> runs = new ArrayList<>();
    private static final Gson gson = new Gson();

    public static RunData GetLastRunData(AbstractPlayer.PlayerClass chosenClass, int minimumAscension)
    {
        RefreshData(chosenClass);

        final RunData data = runs.size() > 0 ? runs.get(0) : null;
        return (data != null && ((minimumAscension == 0) || data.ascension_level >= minimumAscension)) ? data : null;
    }

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

    private static void RefreshData(AbstractPlayer.PlayerClass playerClass) // copied and modified from RunHistoryScreen.refreshData()
    {
        runs.clear();

        final FileHandle[] subFolders = Gdx.files.local("runs" + File.separator).list();
        for (FileHandle subFolder : subFolders)
        {
            if (CardCrawlGame.saveSlot == 0)
            {
                if (subFolder.name().contains("1_") || subFolder.name().contains("2_"))
                {
                    continue;
                }
            }
            else
            {
                if (!subFolder.name().contains(CardCrawlGame.saveSlot + "_"))
                {
                    continue;
                }
            }

            for (FileHandle file : subFolder.list())
            {
                try
                {
                    RunData data = gson.fromJson(file.readString(), RunData.class);
                    if (data != null && data.timestamp == null)
                    {
                        data.timestamp = file.nameWithoutExtension();
                        String exampleDaysSinceUnixStr = "17586";
                        boolean assumeDaysSinceUnix = data.timestamp.length() == exampleDaysSinceUnixStr.length();
                        if (assumeDaysSinceUnix)
                        {
                            try
                            {
                                long secondsInDay = 86400L;
                                long days = Long.parseLong(data.timestamp);
                                data.timestamp = Long.toString(days * secondsInDay);
                            }
                            catch (NumberFormatException var18)
                            {
                                JUtils.LogError(RunHistoryScreenPatches.class, "Run file " + file.path() + " name is could not be parsed into a Timestamp.");
                                data = null;
                            }
                        }
                    }

                    if (data != null && data.character_chosen.equals(playerClass.name()) && GameUtilities.IsNormalRun(data, true))
                    {
                        runs.add(data);
                    }
                }
                catch (JsonSyntaxException var19)
                {
                    JUtils.LogError(RunHistoryScreenPatches.class, "Failed to load RunData from JSON file: " + file.path());
                }
            }
        }

        if (runs.size() > 0)
        {
            runs.sort(RunData.orderByTimestampDesc);
        }
    }
}

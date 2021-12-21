package pinacolada.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

// Act 5 and Act 3 victory logic
// TODO add longest combo bonus here
public class VictoryPatches
{
    private static final FieldInfo<ArrayList<GameOverStat>> _stats = PCLJUtils.GetField("stats", GameOverScreen.class);
    private static final FieldInfo<Integer> _bossPoints = PCLJUtils.GetField("bossPoints", GameOverScreen.class);
    private static final TheUnnamed.Data data = new TheUnnamed.Data(TheUnnamed.ID);

    private static GameOverStat GetUnnamedGameOverStats()
    {
        return new GameOverStat(data.strings.NAME, data.strings.DIALOG[28], String.valueOf(GetUnnamedScoreBonus()));
    }

    private static GameOverStat GetLongestComboStats()
    {
        return new GameOverStat(GR.PCL.Strings.Combat.Rerolls, (String)null, String.valueOf(GR.PCL.Dungeon.GetLongestMatchCombo()));
    }

    private static int GetUnnamedScoreBonus()
    {
        return 300 + Math.round(200 * (PCLGameUtilities.GetAscensionLevel() / 20f));
    }

    @SpirePatch(clz = VictoryRoom.class, method = "onPlayerEntry")
    public static class VictoryRoomPatches_onEnterRoom
    {
        @SpirePrefixPatch
        public static void Method(VictoryRoom __instance)
        {
            if (Settings.isStandardRun() && __instance.eType == VictoryRoom.EventType.HEART // this is the room you enter after defeating act 3 Boss
                && AbstractDungeon.player.chosenClass == GR.PCL.PlayerClass)
            {
                GR.PCL.Data.RecordVictory(PCLGameUtilities.GetActualAscensionLevel());
            }
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryScreenPatches_createGameOverStats
    {
        @SpirePostfixPatch
        public static void Method(VictoryScreen __instance)
        {
            ArrayList<GameOverStat> stats = _stats.Get(__instance);
            stats.add(Math.max(0, stats.size() - 2), GetLongestComboStats());
            if (GR.PCL.Dungeon.IsUnnamedReign())
            {
                _bossPoints.Set(__instance, _bossPoints.Get(__instance) + GetUnnamedScoreBonus());
                stats.add(Math.max(0, stats.size() - 2), GetUnnamedGameOverStats());
            }

            if (Settings.isStandardRun() && PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass))
            {
                GR.PCL.Data.RecordTrueVictory(PCLGameUtilities.GetActualAscensionLevel());
            }
        }
    }


   @SpirePatch(clz = GameOverScreen.class, method = "checkScoreBonus", paramtypez = {boolean.class})
    public static class GameOverScreenPatches_checkScoreBonus
    {
        @SpireInsertPatch(rloc = 1, localvars = {"points"})
        public static void Method(boolean isVictory, @ByRef int[] points)
        {
            points[0] += GR.PCL.Dungeon.GetLongestMatchCombo();
        }
   }
}

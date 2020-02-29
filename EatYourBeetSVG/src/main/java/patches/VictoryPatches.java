package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

// Act 5 and Act 3 victory logic
public class VictoryPatches
{
    private static final TheUnnamed.Data data = new TheUnnamed.Data(TheUnnamed.ID);

    private static GameOverStat GetUnnamedGameOverStats()
    {
        return new GameOverStat(data.strings.NAME, data.strings.DIALOG[28], String.valueOf(GetUnnamedScoreBonus()));
    }

    private static int GetUnnamedScoreBonus()
    {
        return 300 + Math.round(200 * (GameUtilities.GetAscensionLevel() / 20f));
    }

    @SpirePatch(clz = VictoryRoom.class, method = "onPlayerEntry")
    public static class VictoryRoomPatches_onEnterRoom
    {
        @SpirePrefixPatch
        public static void Method(VictoryRoom __instance)
        {
            if (Settings.isStandardRun() && __instance.eType == VictoryRoom.EventType.HEART // this is the room you enter after defeating act 3 Boss
                && AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass)
            {
                GR.Animator.Data.RecordVictory(GameUtilities.GetActualAscensionLevel());
            }
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryScreenPatches_createGameOverStats
    {
        @SpirePostfixPatch
        public static void Method(VictoryScreen __instance)
        {
            if (GR.Common.Dungeon.IsUnnamedReign())
            {
                int index = Math.max(0, __instance.stats.size() - 2);

                __instance.stats.add(index, GetUnnamedGameOverStats());
            }

            if (Settings.isStandardRun() && AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass)
            {
                GR.Animator.Data.RecordTrueVictory(GameUtilities.GetActualAscensionLevel());
            }
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "checkScoreBonus", paramtypez = {boolean.class})
    public static class VictoryScreenPatches_checkScoreBonus
    {
        @SpireInsertPatch(rloc = 1, localvars = {"points"})
        public static void Method(boolean isVictory, @ByRef int[] points)
        {
            if (GR.Common.Dungeon.IsUnnamedReign())
            {
                points[0] += GetUnnamedScoreBonus();
            }
        }
    }
}

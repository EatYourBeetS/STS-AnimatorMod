package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class VictoryPatches
{
    @SpirePatch(clz = VictoryRoom.class, method = "onPlayerEntry")
    public static class VictoryRoomPatches_onEnterRoom
    {
        @SpirePrefixPatch
        public static void Method(VictoryRoom __instance)
        {
            if (Settings.isStandardRun() && __instance.eType == VictoryRoom.EventType.HEART // this is the room you enter after defeating act 3 Boss
                && AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass)
            {
                GR.Animator.Database.OnVictory(GameUtilities.GetActualAscensionLevel());
            }
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryScreenPatches_createGameOverStats
    {
        @SpirePrefixPatch
        public static void Method(VictoryScreen __instance)
        {
            if (Settings.isStandardRun() && AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass)
            {
                GR.Animator.Database.OnTrueVictory(GameUtilities.GetActualAscensionLevel());
            }
        }
    }
}

package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.characters.AnimatorCharacterSelect;
import eatyourbeets.powers.PlayerStatistics;

public class VictoryScreenPatches
{
    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryScreenPatches_createGameOverStats
    {
        @SpirePrefixPatch
        public static void Method(VictoryScreen __instance)
        {
            if (AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR && Settings.isStandardRun())
            {
                AnimatorCharacterSelect.OnTrueVictory(PlayerStatistics.GetActualAscensionLevel());
            }
        }
    }
}

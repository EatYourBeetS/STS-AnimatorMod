package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.characters.AnimatorCharacterSelect;

@SpirePatch(clz= VictoryScreen.class, method = "createGameOverStats")
public class VictoryScreenPatch
{
    @SpirePrefixPatch
    public static void Method(VictoryScreen __instance)
    {
        if (AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR && Settings.isStandardRun())
        {
            AnimatorCharacterSelect.OnTrueVictory(AbstractDungeon.isAscensionMode ? AbstractDungeon.ascensionLevel : 0);
        }
    }
}
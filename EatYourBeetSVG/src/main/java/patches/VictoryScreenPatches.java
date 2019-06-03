package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.characters.AnimatorCharacterSelect;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.powers.PlayerStatistics;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
                AnimatorCharacterSelect.OnTrueVictory(AbstractDungeon.isAscensionMode ? AbstractDungeon.ascensionLevel : 0);
            }
        }
    }
}

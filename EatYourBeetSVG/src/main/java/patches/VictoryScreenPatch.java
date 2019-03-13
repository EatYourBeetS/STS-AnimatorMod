package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import eatyourbeets.characters.AnimatorCharacterSelect;

@SpirePatch(clz= VictoryScreen.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {MonsterGroup.class})
public class VictoryScreenPatch
{
    @SpirePostfixPatch
    public static void Method(VictoryScreen __instance, MonsterGroup useless)
    {
        if (AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR && Settings.isStandardRun())
        {
            AnimatorCharacterSelect.OnTrueVictory(AbstractDungeon.ascensionLevel);
        }
    }
}
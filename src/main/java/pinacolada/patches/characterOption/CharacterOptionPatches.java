package pinacolada.patches.characterOption;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import pinacolada.ui.characterSelection.PCLCharacterSelectScreen;

public class CharacterOptionPatches
{

    @SpirePatch(clz = CharacterOption.class, method = "decrementAscensionLevel")
    public static class CharacterOptionPatches_DecrementAscensionLevel
    {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance)
        {
            PCLCharacterSelectScreen.UpdateForAscensionChange(CardCrawlGame.mainMenuScreen.charSelectScreen);
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "incrementAscensionLevel")
    public static class CharacterOptionPatches_IncrementAscensionLevel
    {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance)
        {
            PCLCharacterSelectScreen.UpdateForAscensionChange(CardCrawlGame.mainMenuScreen.charSelectScreen);
        }
    }
}
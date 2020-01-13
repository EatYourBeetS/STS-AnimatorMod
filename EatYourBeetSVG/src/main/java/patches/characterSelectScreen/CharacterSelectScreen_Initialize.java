package patches.characterSelectScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.ui.screens.animator.characterSelection.AnimatorCharacterSelectScreen;

@SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
public class CharacterSelectScreen_Initialize
{
    @SpirePostfixPatch
    public static void Initialize(CharacterSelectScreen __instance)
    {
        AnimatorCharacterSelectScreen.Initialize(__instance);
    }
}
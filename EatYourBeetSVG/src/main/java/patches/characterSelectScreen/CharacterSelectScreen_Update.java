package patches.characterSelectScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.ui.animator.characterSelect.AnimatorCharacterSelectScreen;

@SpirePatch(clz = CharacterSelectScreen.class, method = "update")
public class CharacterSelectScreen_Update
{
    @SpirePostfixPatch
    public static void Postfix(CharacterSelectScreen __instance)
    {
        AnimatorCharacterSelectScreen.Update(__instance);
    }
}
package patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.ui.screens.animator.characterSelection.AnimatorCharacterSelectScreen;

public class CharacterSelectScreenPatches
{
    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class CharacterSelectScreen_Initialize
    {
        @SpirePostfixPatch
        public static void Initialize(CharacterSelectScreen __instance)
        {
            AnimatorCharacterSelectScreen.Initialize(__instance);
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class CharacterSelectScreen_Render
    {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb)
        {
            AnimatorCharacterSelectScreen.Render(__instance, sb);
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class CharacterSelectScreen_Update
    {
        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance)
        {
            AnimatorCharacterSelectScreen.Update(__instance);
        }
    }
}

package patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.characterSelection.AnimatorCharacterSelectScreen;

public class CharacterOptionPatches
{
    @SpirePatch(clz = CharacterOption.class, method = "render")
    public static class CharacterOption_Render
    {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance, SpriteBatch sb)
        {
            if (__instance.selected && __instance.c != null && __instance.c.chosenClass == GR.Animator.PlayerClass)
            {
                AnimatorCharacterSelectScreen.RenderOption(__instance, sb);
            }
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "update")
    public static class CharacterOption_Update
    {
        @SpirePostfixPatch
        public static void Postfix(CharacterOption __instance)
        {
            if (__instance.selected && __instance.c != null && __instance.c.chosenClass == GR.Animator.PlayerClass)
            {
                AnimatorCharacterSelectScreen.UpdateOption(__instance);
            }
        }
    }
}

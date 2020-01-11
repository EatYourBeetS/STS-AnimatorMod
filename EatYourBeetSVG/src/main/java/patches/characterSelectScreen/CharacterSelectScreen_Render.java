package patches.characterSelectScreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.ui.animator.characterSelect.AnimatorCharacterSelectScreen;

@SpirePatch(clz = CharacterSelectScreen.class, method = "render")
public class CharacterSelectScreen_Render
{
    @SpirePostfixPatch
    public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb)
    {
        AnimatorCharacterSelectScreen.Render(__instance, sb);
    }
}
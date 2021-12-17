package pinacolada.patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import pinacolada.resources.GR;
import pinacolada.ui.characterSelection.PCLCharacterSelectScreen;

public class CharacterSelectScreenPatches
{
    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class CharacterSelectScreen_Initialize
    {
        @SpirePostfixPatch
        public static void Initialize(CharacterSelectScreen __instance)
        {
            PCLCharacterSelectScreen.Initialize(__instance);
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
    public static class CharacterSelectScreen_Render
    {
//        @SpirePrefixPatch
//        public static SpireReturn Prefix(CharacterSelectScreen __instance, SpriteBatch sb)
//        {
//            return AbstractDungeon.isScreenUp ? SpireReturn.Return() : SpireReturn.Continue();
//        }

        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb)
        {
            PCLCharacterSelectScreen.Render(__instance, sb);
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
    public static class CharacterSelectScreen_Update
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(CharacterSelectScreen __instance)
        {
            return (GR.UI.CurrentScreen != null) ? SpireReturn.Return() : SpireReturn.Continue();
        }

        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance)
        {
            PCLCharacterSelectScreen.Update(__instance);
        }
    }
}

package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.ui.GridCardSelectScreenPatch;

public class GridCardSelectScreenPatches
{
    @SpirePatch(clz = GridCardSelectScreen.class, method = "callOnOpen")
    public static class GridCardSelectScreenPatch_Open
    {
        @SpirePostfixPatch
        public static void Postfix(GridCardSelectScreen __instance)
        {
            GridCardSelectScreenPatch.Open(__instance);
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="update")
    public static class GridCardSelectScreenPatch_Update
    {
        @SpirePostfixPatch
        public static void Postfix(GridCardSelectScreen __instance)
        {
            GridCardSelectScreenPatch.Update(__instance);
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="render")
    public static class GridCardSelectScreenPatch_Render
    {
        @SpirePostfixPatch
        public static void Postfix(GridCardSelectScreen __instance, SpriteBatch sb)
        {
            GridCardSelectScreenPatch.Render(__instance, sb);
        }
    }
}
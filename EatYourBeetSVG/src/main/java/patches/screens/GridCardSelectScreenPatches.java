package patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.ui.GridCardSelectScreenPatch;
import eatyourbeets.utilities.GameUtilities;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class GridCardSelectScreenPatches
{
    @SpirePatch(clz= GridCardSelectScreen.class, method="calculateScrollBounds")
    public static class GridCardSelectScreen_CalculateScrollBounds
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(GridCardSelectScreen __instance)
        {
            if (GridCardSelectScreenPatch.CalculateScrollBounds(__instance))
            {
                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="callOnOpen")
    public static class GridCardSelectScreen_CallOnOpen
    {
        @SpirePostfixPatch
        public static void Postfix(GridCardSelectScreen __instance)
        {
            GridCardSelectScreenPatch.Open(__instance);
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="updateCardPositionsAndHoverLogic")
    public static class GridCardSelectScreen_UpdateCardPositionsAndHoverLogic
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(GridCardSelectScreen __instance)
        {
            if (GridCardSelectScreenPatch.UpdateCardPositionAndHover(__instance))
            {
                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz= GridCardSelectScreen.class, method="render")
    public static class GridCardSelectScreen_Render
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GridCardSelectScreen __instance, SpriteBatch sb)
        {
            if (!GameUtilities.IsTopPanelVisible())
            {
                final float offset_y = 64.0F * Settings.scale;
                sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, Settings.HEIGHT - offset_y, (float) Settings.WIDTH, offset_y);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
            {
                final Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}

package patches.abstractDungeon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz = AbstractDungeon.class, method = "render", paramtypez = {SpriteBatch.class})
public class AbstractDungeon_Render
{
//    @SpireInsertPatch(locator=Locator.class)
//    public static void Insert(AbstractDungeon __instance, SpriteBatch sb)
//    {
//        if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN)
//        {
//            GR.UI.Render(sb);
//        }
//    }
//
//    private static class Locator extends SpireInsertLocator
//    {
//        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
//        {
//            Matcher finalMatcher = new Matcher.MethodCallMatcher(Logger.class, "info");
//
//            int[] res = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
//            res[0] += 1; // replace
//            return res;
//        }
//    }
}
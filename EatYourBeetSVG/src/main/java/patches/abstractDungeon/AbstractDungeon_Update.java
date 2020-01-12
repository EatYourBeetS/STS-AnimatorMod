package patches.abstractDungeon;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.Logger;

@SpirePatch(clz = AbstractDungeon.class, method = "update")
public class AbstractDungeon_Update
{
    @SpireInsertPatch(locator= Locator.class)
    public static void Insert(AbstractDungeon __instance)
    {
        if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN)
        {
            GR.Screens.Update();
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Logger.class, "info");

            int[] res = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            res[0] += 1; // replace
            return res;
        }
    }
}
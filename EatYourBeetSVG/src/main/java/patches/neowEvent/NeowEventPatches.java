package patches.neowEvent;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.neow.NeowEvent;
import javassist.CtBehavior;

public class NeowEventPatches {

    @SpirePatch(clz = NeowEvent.class, method = "dailyBlessing")
    public static class NeowEvent_DailyBlessing
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(NeowEvent __instance)
        {
            //Add mods that must appear when Neow gives his blessing here.

        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
                int[] linesFound = LineFinder.findInOrder(ctBehavior, matcher);
                return new int[]{ linesFound[0] - 1 };
            }
        }
    }
}

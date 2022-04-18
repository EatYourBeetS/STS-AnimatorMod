package patches.effects;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import eatyourbeets.utilities.GameUtilities;
import javassist.CtBehavior;

public class CampfireSmithEffectPatches
{
    @SpirePatch(clz = CampfireSmithEffect.class, method = "update")
    public static class CampfireSmithEffectPatches_Update
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Method(CampfireSmithEffect __instance)
        {
            GameUtilities.GetAscensionData(true).OnSmith();
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            final Matcher finalMatcher = new Matcher.MethodCallMatcher(MetricData.class, "addCampfireChoiceData");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
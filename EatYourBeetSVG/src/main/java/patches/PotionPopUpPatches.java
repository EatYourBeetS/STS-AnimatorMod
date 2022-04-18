package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import eatyourbeets.utilities.GameUtilities;
import javassist.CtBehavior;

public class PotionPopUpPatches
{
    @SpirePatch(clz = PotionPopUp.class, method = "updateTargetMode")
    public static class PotionPopUpPatches_UpdateTargetMode
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Method(PotionPopUp __instance)
        {
            GameUtilities.GetAscensionData(true).OnUsePotion();
        }
    }

    @SpirePatch(clz = PotionPopUp.class, method = "updateInput")
    public static class PotionPopUpPatches_UpdateInput
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Method(PotionPopUp __instance)
        {
            GameUtilities.GetAscensionData(true).OnUsePotion();
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            final Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPotion.class, "use");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
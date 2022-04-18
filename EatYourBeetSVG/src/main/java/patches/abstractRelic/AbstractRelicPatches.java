package patches.abstractRelic;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class AbstractRelicPatches
{
    @SpirePatch(clz = AbstractRelic.class, method = "getPrice")
    public static class AbstractRelicPatches_GetPrice
    {
        @SpirePrefixPatch
        public static SpireReturn Insert(AbstractRelic __instance)
        {
            if (GameUtilities.IsEYBPlayerClass() && __instance.tier == AbstractRelic.RelicTier.BOSS)
            {
                return SpireReturn.Return(333);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "update")
    public static class AbstractRelicPatches_Update
    {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn Insert(AbstractRelic __instance)
        {
            if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN)
            {
                __instance.scale = MathHelper.scaleLerpSnap(__instance.scale, Settings.scale);
                __instance.hb.unhover();

                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
            {
            /*
            ...

            <REPLACING_HERE>

            if (AbstractDungeon.player != null && AbstractDungeon.player.relics.indexOf(this) / 25 == relicPage)
            {
                this.hb.update();
            }
            else
            {
                this.hb.hovered = false;
            }

            ...
            */

                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
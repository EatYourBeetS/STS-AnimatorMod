package patches.abstractRelic;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.resources.GR;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractRelic.class, method = "update")
public class AbstractRelic_Update
{
    @SpireInsertPatch(locator= Locator.class)
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
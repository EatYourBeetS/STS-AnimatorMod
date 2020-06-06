package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;
import javassist.CtBehavior;

public class UpdateControlPileCard {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderPlayerBattleUi"
    )
    public static class stopPlayerUI
    {
        @SpirePrefixPatch
        public static SpireReturn<Void> stop(AbstractPlayer __instance, SpriteBatch sb)
        {
            //makes it so power tips don't show up while the player is hovering the control pile cards
            if (CombatStats.ControlPile.IsHovering()) {
                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = AbstractOrb.class,
            method = "update"
    )
    public static class stopOrbTips
    {
        @SpireInsertPatch(locator = stopOrbTips.Locator.class)
        public static SpireReturn<Void> stop(AbstractOrb __instance)
        {
            //makes it so orb tips don't show up while the player is hovering the control pile cards
            if (CombatStats.ControlPile.IsHovering()) {
                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TipHelper.class, "renderGenericTip");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
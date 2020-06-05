package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;
import javassist.CtBehavior;

public class UpdateControlPileCard {

    //These patches are needed because calling applyPowers and glowcheck in OnPhaseChange wasn't good enough
    @SpirePatch(
            clz = CardGroup.class,
            method = "applyPowers"
    )
    public static class ApplyPowers
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                card.applyPowers();
            }
        }
    }
    @SpirePatch(
            clz = CardGroup.class,
            method = "glowCheck"
    )
    public static class GlowCheck
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                glowCheck(card);
                card.triggerOnGlowCheck();
            }
        }
    }

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

    public static void glowCheck(AbstractCard c)
    {
        if (c.canUse(AbstractDungeon.player, null) && !AbstractDungeon.isScreenUp) {
            c.beginGlowing();
        } else {
            c.stopGlowing();
        }
    }
}
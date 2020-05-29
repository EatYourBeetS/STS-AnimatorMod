package patches.ControlPilePatches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;
import javassist.CtBehavior;


public class UpdateControlPileCard {

    @SpirePatch(
            clz = CardGroup.class,
            method = SpirePatch.CLASS
    )

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
            clz = CardGroup.class,
            method = "updateHoverLogic"
    )
    public static class UpdateHoverLogic
    {
        @SpirePostfixPatch
        public static void update(CardGroup __instance)
        {
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                card.updateHoverLogic();
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "refreshHandLayout"
    )
    public static class RefreshLayout
    {
        @SpireInsertPatch(
                locator = HoverLocator.class
        )
        public static void onRefreshLayout(CardGroup __instance)
        {
            CombatStats.ControlPile.setCardPositions();
        }

        private static class HoverLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredCard");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "canUseAnyCard"
    )
    public static class MaybeYouCan
    {
        @SpirePostfixPatch
        public static boolean maybe(boolean __result, CardGroup __instance)
        {
            boolean hasEnoughEnergy = false;
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                if (card.hasEnoughEnergy()) {
                    hasEnoughEnergy = true;
                }
            }
            return __result || (!AbstractDungeon.player.exhaustPile.isEmpty() && hasEnoughEnergy);
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
package patches.AfterlifePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;
import javassist.CtBehavior;

import java.util.ArrayList;


@SpirePatch(
        clz = AbstractPlayer.class,
        method = "updateInput"
)
public class HoverTopCard {

    @SpireInsertPatch(
            locator = FirstLocator.class
    )
    public static void GetHoverOtherHand(AbstractPlayer __instance)
    {
        if (__instance.hoveredCard == null && CombatStats.ControlPile.isHovering())
        {
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                card.hb.update();
                if (card.hb.hovered) {
                    for (CardQueueItem q : AbstractDungeon.actionManager.cardQueue)
                    {
                        if (q.card == card)
                            return;
                    }
                    __instance.hoveredCard = card;
                }
            }
        }
    }

    @SpireInsertPatch(
            locator = AngleLocator.class
    )
    public static void reposition(AbstractPlayer __instance)
    {
        for (ControllableCard c : CombatStats.ControlPile.controllers) {
            AbstractCard card = c.card;
            if (__instance.hoveredCard.equals(card)) {
                __instance.hoveredCard.current_x = __instance.hoveredCard.target_x + (60.0F * Settings.scale);
                __instance.hoveredCard.target_x = __instance.hoveredCard.target_x + (60.0F * Settings.scale);
                __instance.hoveredCard.current_y = __instance.hoveredCard.target_y + (70.0F * Settings.scale);
                __instance.hoveredCard.target_y = __instance.hoveredCard.target_y + (70.0F * Settings.scale);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "hoverCardPush"
    )
    public static class NoPush
    {
        @SpirePrefixPatch
        public static SpireReturn no(CardGroup __instance, AbstractCard cardToCheck)
        {
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                if (cardToCheck.equals(card)) {
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }

    private static class FirstLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            ArrayList<Matcher> requiredMatches = new ArrayList<>();
            requiredMatches.add(new Matcher.MethodCallMatcher(CardGroup.class, "getHoveredCard"));
            requiredMatches.add(new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredCard"));

            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredCard");

            return LineFinder.findInOrder(ctMethodToPatch, requiredMatches, finalMatcher);
        }
    }
    private static class AngleLocator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "setAngle");

            ArrayList<Matcher> requiredMatches = new ArrayList<>();
            requiredMatches.add(finalMatcher);

            return LineFinder.findInOrder(ctMethodToPatch, requiredMatches, finalMatcher);
        }
    }
}
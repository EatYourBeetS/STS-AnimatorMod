package patches.AfterlifePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;


@SpirePatch(
        clz = AbstractPlayer.class,
        method = "updateInput"
)
public class HoverTopCard {
    private static final float HOVERED_X_POSITION = 180 * Settings.scale;
    private static final float HOVERED_Y_POSITION = 470 * Settings.scale;

    @SpireInsertPatch(
            locator = FirstLocator.class
    )
    public static void GetHoverOtherHand(AbstractPlayer __instance)
    {
        if (__instance.hoveredCard == null)
        {
            ArrayList<AbstractCard> c = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                for (AbstractCard card : c) {
                    card.hb.update();
                    if (card.hb.hovered) {
                        for (CardQueueItem q : AbstractDungeon.actionManager.cardQueue)
                        {
                            if (q.card == card)
                                return;
                        }
                        System.out.println(__instance.hoveredCard);
                        System.out.println(card);
                        __instance.hoveredCard = card;
                    }
                }
            }
        }
    }

    @SpireInsertPatch(
            locator = AngleLocator.class
    )
    public static void reposition(AbstractPlayer __instance)
    {
        ArrayList<AbstractCard> c = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
        if (c != null) {
            for (AbstractCard card : c) {
                if (__instance.hoveredCard.equals(card)) {
                    __instance.hoveredCard.current_x = __instance.hoveredCard.target_x + (60.0F * Settings.scale);
                    System.out.println(__instance.hoveredCard.current_x);
                    __instance.hoveredCard.target_x = HOVERED_X_POSITION;
                    __instance.hoveredCard.current_y = HOVERED_Y_POSITION;
                    __instance.hoveredCard.target_y = HOVERED_Y_POSITION;
                }
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
            ArrayList<AbstractCard> c = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                for (AbstractCard card : c) {
                    if (cardToCheck.equals(card)) {
                        return SpireReturn.Return(null);
                    }
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
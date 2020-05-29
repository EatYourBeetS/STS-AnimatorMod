package patches.AfterlifePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.ArrayList;


public class PlayTopCard {
    private static final float MIN_DROP_X = 300 * Settings.scale;

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "updateInput"
    )
    public static class ModifyDropZone
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void modify(AbstractPlayer __instance)
        {
            ArrayList<AbstractCard> c = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                for (AbstractCard card : c) {
                    if (__instance.hoveredCard != null && __instance.hoveredCard.equals(card)) {
                        if (InputHelper.mX < MIN_DROP_X)
                        {
                            __instance.isHoveringDropZone = false;
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDraggingCard");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard"
    )
    public static class RemoveCard
    {
        @SpirePostfixPatch
        public static void remove(AbstractPlayer __instance, AbstractCard cardPlayed, AbstractMonster m, int e)
        {
            AbstractCard cardToRemove = null;
            ArrayList<AbstractCard> c = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                for (AbstractCard card : c) {
                    if (cardPlayed.equals(card)) {
                        for (AbstractCard cardToCheck : AbstractDungeon.player.exhaustPile.group) {
                            if (cardToCheck.uuid == card.uuid) {
                                cardToRemove = cardToCheck;
                            }
                        }
                        __instance.exhaustPile.removeCard(cardToRemove);
                    }
                }
            }
        }
    }
}
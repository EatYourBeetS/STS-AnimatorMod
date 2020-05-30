package patches.ControlPilePatches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;
import javassist.CtBehavior;


public class PlayControlPileCard {
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
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                if (__instance.hoveredCard != null && __instance.hoveredCard.equals(card)) {
                    if (InputHelper.mX < MIN_DROP_X)
                    {
                        __instance.isHoveringDropZone = false;
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
            for (ControllableCard c : CombatStats.ControlPile.controllers) {
                AbstractCard card = c.card;
                if (cardPlayed.equals(card)) {
                    if (c.originGroup != null) {
                        for (AbstractCard cardToCheck : c.originGroup.group) {
                            if (cardToCheck.uuid == card.uuid) {
                                cardToRemove = cardToCheck;
                            }
                        }
                        c.originGroup.removeCard(cardToRemove);
                    }
                }
            }
        }
    }
}
package patches.AfterlifePatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;


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
            if (__instance.hoveredCard != null && __instance.hoveredCard.equals(UpdateAndTrackTopCard.Fields.currentCard.get(__instance.exhaustPile)))
            {
                if (InputHelper.mX < MIN_DROP_X)
                {
                    __instance.isHoveringDropZone = false;
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
        public static void remove(AbstractPlayer __instance, AbstractCard c, AbstractMonster m, int e)
        {
            if (c.equals(UpdateAndTrackTopCard.Fields.currentCard.get(__instance.exhaustPile)))
            {
                __instance.exhaustPile.removeCard(UpdateAndTrackTopCard.Fields.originalCurrentCard.get(__instance.exhaustPile));
            }
        }
    }
}
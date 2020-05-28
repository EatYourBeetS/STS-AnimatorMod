package patches.AfterlifePatches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import javassist.CtBehavior;

import java.util.ArrayList;


//DONE:
/*
    card's update method is called appropriately
    card's render method is called appropriately (RenderTopCard.java)
    card's position is updated when which card it is changes, and when RefreshHandLayout is called
    card's hover logic is updated when hover logic is updated


 */


public class UpdateAndTrackTopCard {
    private static final float RENDER_X = 1800 * Settings.scale;
    private static final float RENDER_Y = 400 * Settings.scale;

    @SpirePatch(
            clz = CardGroup.class,
            method = SpirePatch.CLASS
    )
    public static class Fields
    {
        public static SpireField<AbstractCard> originalCurrentCard = new SpireField<>(()->null);
        public static SpireField<AbstractCard> currentCard = new SpireField<>(()->null);
    }

    @SpirePatch(
            clz = ExhaustPanel.class,
            method = "updatePositions"
    )
    public static class Update
    {
        @SpirePostfixPatch
        public static void doTheUpdateThing(ExhaustPanel __instance) {
            if (!AbstractDungeon.player.exhaustPile.isEmpty()) {
                AbstractCard top = AbstractDungeon.player.exhaustPile.getTopCard();
                AbstractCard last = Fields.originalCurrentCard.get(AbstractDungeon.player.exhaustPile);
                //checks to see if the card has changed
                if (!top.equals(last)) {
                    if (last != null) {
                        partialReset(last);
                        last.shrink();
                    }
                    Fields.originalCurrentCard.set(AbstractDungeon.player.exhaustPile, top);
                    //We make a clone and render it to the player so we don't get the
                    //"card is in two places at once visual glitch"
                    AbstractCard clone = top.makeStatEquivalentCopy();
                    Fields.currentCard.set(AbstractDungeon.player.exhaustPile, clone);
                    glowCheck(clone);
                    setPosition(clone);
                }
                Fields.currentCard.get(AbstractDungeon.player.exhaustPile).update();
            } else {
                Fields.originalCurrentCard.set(AbstractDungeon.player.exhaustPile, null);
                Fields.currentCard.set(AbstractDungeon.player.exhaustPile, null);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "applyPowers"
    )
    public static class ApplyPowers
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
           AbstractCard c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
           if (c != null) {
               c.applyPowers();
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
            AbstractCard c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
               glowCheck(c);
               c.triggerOnGlowCheck();
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
            AbstractCard c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                c.updateHoverLogic();
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
            AbstractCard c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                setPosition(c);
            }
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
            return __result || (!AbstractDungeon.player.exhaustPile.isEmpty() && Fields.currentCard.get(AbstractDungeon.player.exhaustPile).hasEnoughEnergy());
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
    public static void partialReset(AbstractCard c)
    {
        c.block = c.baseBlock;
        c.isBlockModified = false;
        c.damage = c.baseDamage;
        c.isDamageModified = false;
        c.magicNumber = c.baseMagicNumber;
        c.isMagicNumberModified = false;

        c.stopGlowing();
    }
    public static void setPosition(AbstractCard c)
    {
        c.targetDrawScale = 0.5f;
        c.targetAngle = 0;
        c.target_x = RENDER_X;
        c.target_y = RENDER_Y;
    }
}
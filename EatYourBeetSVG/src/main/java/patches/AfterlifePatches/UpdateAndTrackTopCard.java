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
    public static final float RENDER_X_OFFSET = 200.0F * Settings.scale;
    private static final float RENDER_X = 120 * Settings.scale;
    private static final float RENDER_Y = 400 * Settings.scale;

    @SpirePatch(
            clz = CardGroup.class,
            method = SpirePatch.CLASS
    )
    public static class Fields
    {
        //public static SpireField<ArrayList<AbstractCard>> originalCurrentCard = new SpireField<>(()->null);
        public static SpireField<Integer> oldSize = new SpireField<>(()->null);
        public static SpireField<ArrayList<AbstractCard>> currentCard = new SpireField<>(()->null);
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
                int top = AbstractDungeon.player.exhaustPile.group.size();
                Integer last = Fields.oldSize.get(AbstractDungeon.player.exhaustPile);
                //checks to see if the card has changed
                if (top != last) {
                    System.out.println("stuff changed, updating");
                    System.out.println(top);
                    System.out.println(last);
//                    ArrayList<AbstractCard> oldCards = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
//                    if (oldCards != null) {
//                        for (AbstractCard card : oldCards) {
//                            partialReset(card);
//                            card.shrink();
//                        }
//                    }
                    Fields.oldSize.set(AbstractDungeon.player.exhaustPile, top);
                    //We make a clone and render it to the player so we don't get the
                    //"card is in two places at once visual glitch"
                    ArrayList<AbstractCard> clone = new ArrayList<>();
                    for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
                        AbstractCard clonedCard = card.makeStatEquivalentCopy();
                        clonedCard.uuid = card.uuid;
                        clone.add(clonedCard);
                    }
                    Fields.currentCard.set(AbstractDungeon.player.exhaustPile, clone);
                    setPosition(clone);
                    for (AbstractCard card : clone) {
                        glowCheck(card);
                    }
                }
                for (AbstractCard card : Fields.currentCard.get(AbstractDungeon.player.exhaustPile)) {
                    card.update();
                }
            } else {
                Fields.oldSize.set(AbstractDungeon.player.exhaustPile, -1);
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
           ArrayList<AbstractCard> c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
           if (c != null) {
               for (AbstractCard card : c) {
                   card.applyPowers();
               }
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
            ArrayList<AbstractCard> c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                for (AbstractCard card : c) {
                    glowCheck(card);
                    card.triggerOnGlowCheck();
                }
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
            ArrayList<AbstractCard> c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                for (AbstractCard card : c) {
                    card.updateHoverLogic();
                }
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
            ArrayList<AbstractCard> c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
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
            boolean hasEnoughEnergy = false;
            ArrayList<AbstractCard> c = Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
            if (c != null) {
                for (AbstractCard card : c) {
                    if (card.hasEnoughEnergy()) {
                        hasEnoughEnergy = true;
                    }
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
    public static void setPosition(ArrayList<AbstractCard> c)
    {
        System.out.println("setting positions");
        int count = 0;
        for (AbstractCard card : c) {
            card.targetDrawScale = 0.5f;
            card.targetAngle = 0;
            card.target_x = RENDER_X + RENDER_X_OFFSET * count;
            card.target_y = RENDER_Y;
            count++;
        }

    }
}
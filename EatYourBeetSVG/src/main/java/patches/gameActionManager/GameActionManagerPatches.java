package patches.gameActionManager;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import javassist.CtBehavior;

import java.util.ArrayList;

public class GameActionManagerPatches
{
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GameActionManager_GetNextAction
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GameActionManager __instance)
        {
            final CardQueueItem item = __instance.cardQueue.get(0);
            if (item.card.hasTag(GR.Enums.CardTags.AUTOPLAYED))
            {
                if (item.card.isInAutoplay && !item.ignoreEnergyTotal && !item.card.freeToPlay() && item.card.costForTurn > 0)
                {
                    AbstractDungeon.player.energy.use(item.card.costForTurn);
                }
            }

            CombatStats.ResetAfterPlay().add(item.card);
        }

//        public static class Locator extends SpireInsertLocator
//        {
//            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
//            {
//                final Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "dontTriggerOnUseCard");
//                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher)[1]};
//            }
//        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "useCard");
                return new int[]{ LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] + 1 };
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "incrementDiscard", paramtypez = {boolean.class})
    public static class GameActionManager_IncrementDiscard
    {
        @SpirePostfixPatch
        public static void Postfix(boolean endOfTurn)
        {
            if (!AbstractDungeon.actionManager.turnHasEnded && !endOfTurn)
            {
                CombatStats.OnManualDiscard();
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "clearPostCombatActions")
    public static class GameActionManager_ClearPostCombatActions
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(GameActionManager __instance)
        {
            final ArrayList<AbstractGameAction> actions = __instance.actions;
            for (int i = actions.size() - 1; i >= 0; i--)
            {
                AbstractGameAction action = actions.get(i);
                if (action instanceof EYBAction)
                {
                    if (((EYBAction) action).canCancel)
                    {
                        actions.remove(i);
                    }
                }
                else if (!(action instanceof HealAction
                        || action instanceof GainBlockAction
                        || action instanceof UseCardAction
                        || action.actionType == AbstractGameAction.ActionType.DAMAGE))
                {
                    actions.remove(i);
                }
            }

            return SpireReturn.Return(null);
        }
    }
}
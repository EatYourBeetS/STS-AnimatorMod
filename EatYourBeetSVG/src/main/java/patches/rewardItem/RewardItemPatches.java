package patches.rewardItem;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.JUtils;
import javassist.CtBehavior;

public class RewardItemPatches
{
    @SpirePatch(clz = RewardItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {})
    public static class RewardItemPatches_Ctor
    {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn Insert(RewardItem __instance)
        {
            if (__instance instanceof AnimatorReward)
            {
                __instance.text = RewardItem.TEXT[2];
                __instance.cards = null;
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                final Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getRewardCards");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

//    @SpirePatch(clz = RewardItem.class, method = "render", paramtypez = {SpriteBatch.class})
//    public static class RewardItemPatches_Render
//    {
//        @SpirePostfixPatch
//        public static void Postfix(RewardItem __instance, SpriteBatch sb)
//        {
//            if (__instance.hb.hovered && __instance.type == RewardItem.RewardType.RELIC && __instance.relic instanceof EYBRelic)
//            {
//                EYBCardTooltip.CanRenderTooltips(false);
//                EYBCardTooltip.CanRenderTooltips(true);
//                EYBCardTooltip.QueueTooltips((EYBRelic) __instance.relic);
//            }
//        }
//    }

    @SpirePatch(clz = RewardItem.class, method = "update", paramtypez = {})
    public static class RewardItemPatches_Update
    {
        @SpirePostfixPatch
        public static void Postfix(RewardItem __instance)
        {
            if (__instance.hb.hovered && __instance.type == RewardItem.RewardType.RELIC)
            {
                final EYBRelic relic = JUtils.SafeCast(__instance.relic, EYBRelic.class);
                if (relic != null)
                {
                    EYBCardTooltip.CanRenderTooltips(false);
                    EYBCardTooltip.CanRenderTooltips(true);
                    EYBCardTooltip.QueueTooltips(relic);
                    relic.OnRelicHovering(__instance);
                }
            }
        }
    }
}

package patches.rewardItem;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.rewards.AnimatorReward;
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

    @SpirePatch(clz = RewardItem.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class RewardItemPatches_Render
    {
        @SpirePostfixPatch
        public static void Postfix(RewardItem __instance, SpriteBatch sb)
        {
            if (__instance.hb.hovered && __instance.type == RewardItem.RewardType.RELIC && __instance.relic instanceof EYBRelic)
            {
                EYBCardTooltip.CanRenderTooltips(false);
                EYBCardTooltip.CanRenderTooltips(true);
                EYBCardTooltip.QueueTooltips((EYBRelic) __instance.relic);
            }
        }
    }
}

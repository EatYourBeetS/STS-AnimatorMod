package patches.combatRewardScreen;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import eatyourbeets.interfaces.subscribers.OnReceiveRewardsSubscriber;
import javassist.CtBehavior;

@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
public class CombatRewardScreen_SetupItemReward
{
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(CombatRewardScreen __instance)
    {
        final AbstractPlayer p = AbstractDungeon.player;
        for (AbstractRelic r : p.relics)
        {
            if (r instanceof OnReceiveRewardsSubscriber)
            {
                ((OnReceiveRewardsSubscriber) r).OnReceiveRewards(__instance.rewards);
            }
        }

        if (p instanceof OnReceiveRewardsSubscriber)
        {
            ((OnReceiveRewardsSubscriber) p).OnReceiveRewards(__instance.rewards);
        }
    }

    @SuppressWarnings("unused")
    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[0]};
        }
    }
}
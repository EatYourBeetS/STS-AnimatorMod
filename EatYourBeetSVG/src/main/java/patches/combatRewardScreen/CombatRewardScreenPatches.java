package patches.combatRewardScreen;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;
import javassist.CtBehavior;

public class CombatRewardScreenPatches
{
    @SpirePatch(clz = CombatRewardScreen.class, method = "update")
    public static class CombatRewardScreenPatches_Update
    {
        private static final MethodInfo.T0<?> _updateEffects = JUtils.GetMethod("updateEffects", CombatRewardScreen.class);

        @SpirePrefixPatch
        public static SpireReturn Prefix(CombatRewardScreen __instance)
        {
            if (GameEffects.IsEmpty())
            {
                return SpireReturn.Continue();
            }

            _updateEffects.Invoke(__instance);
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class CombatRewardScreenPatches_SetupItemReward
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CombatRewardScreen __instance)
        {
            final AbstractPlayer p = AbstractDungeon.player;
            for (AbstractRelic r : p.relics)
            {
                if (r instanceof OnReceiveRewardsListener)
                {
                    ((OnReceiveRewardsListener) r).OnReceiveRewards(__instance.rewards);
                }
            }

            if (p instanceof OnReceiveRewardsListener)
            {
                ((OnReceiveRewardsListener) p).OnReceiveRewards(__instance.rewards);
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
}
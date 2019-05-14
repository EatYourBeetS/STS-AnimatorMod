package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import eatyourbeets.relics.TheMissingPiece;
import javassist.CtBehavior;

@SpirePatch(clz= CombatRewardScreen.class, method="setupItemReward")
public class CombatRewardScreenPatch
{
    @SpireInsertPatch(locator=Locator.class)
    public static void Insert(CombatRewardScreen __instance)
    {
        TheMissingPiece relic = (TheMissingPiece) AbstractDungeon.player.getRelic(TheMissingPiece.ID);
        if (relic != null)
        {
            relic.receiveRewards(__instance.rewards);
        }
//        PurgingStone_Cards relic2 = (PurgingStone_Cards) AbstractDungeon.player.getRelic(PurgingStone_Cards.ID);
//        if (relic2 != null)
//        {
//            relic2.receiveRewards(__instance.rewards);
//        }
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
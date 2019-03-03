package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.DynamicButton;
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
//        PurgingStone relic2 = (PurgingStone) AbstractDungeon.player.getRelic(PurgingStone.ID);
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
            Matcher finalMatcher = new Matcher.MethodCallMatcher(DynamicButton.class, "hide");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[0]};
        }
    }
}
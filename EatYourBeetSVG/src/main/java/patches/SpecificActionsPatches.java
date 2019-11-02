package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import eatyourbeets.actions.animator.PlayTempBgmAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.Field;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;

public class SpecificActionsPatches
{
    @SpirePatch(clz = ShuffleAction.class, method = "update")
    public static class ShuffleAction_update
    {
        private static final Field<Boolean> triggerRelics = Utilities.GetPrivateField("triggerRelics", ShuffleAction.class);

        @SpirePrefixPatch
        public static void Prefix(ShuffleAction __instance)
        {
            if (!__instance.isDone)
            {
                PlayerStatistics.OnShuffle(triggerRelics.Get(__instance));
            }
        }
    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class EmptyDeckShuffleAction_update
    {
        private static final Field<Boolean> shuffled = Utilities.GetPrivateField("shuffled", EmptyDeckShuffleAction.class);

        @SpirePrefixPatch
        public static void Prefix(EmptyDeckShuffleAction __instance)
        {
            if (!__instance.isDone && !shuffled.Get(__instance))
            {
                PlayerStatistics.OnShuffle(true);
            }
        }
    }
}
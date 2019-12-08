package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

public class SpecificActionsPatches
{
//    @SpirePatch(clz = ShuffleAction.class, method = "update")
//    public static class ShuffleAction_update
//    {
//        private static final FieldInfo<Boolean> triggerRelics = JavaUtilities.GetPrivateField("triggerRelics", ShuffleAction.class);
//
//        @SpirePrefixPatch
//        public static void Prefix(ShuffleAction __instance)
//        {
//            if (!__instance.isDone)
//            {
//                PlayerStatistics.OnShuffle(triggerRelics.Get(__instance));
//            }
//        }
//    }

    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
    public static class EmptyDeckShuffleAction_update
    {
        private static final FieldInfo<Boolean> shuffled = JavaUtilities.GetPrivateField("shuffled", EmptyDeckShuffleAction.class);

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
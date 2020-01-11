package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

@SpirePatch(clz = EmptyDeckShuffleAction.class, method = "update")
public class EmptyDeckShufflePatches
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

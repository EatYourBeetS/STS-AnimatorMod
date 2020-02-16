package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class NeowRoomPatches
{
    @SpirePatch(clz = AbstractEvent.class, method = "onEnterRoom")
    public static class AbstractEvent_OnEnterRoom
    {
        @SpirePostfixPatch
        public static void Prefix(AbstractEvent __instance)
        {
            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && GR.Animator.Dungeon.Series.isEmpty())
            {
                GR.UI.SeriesSelection.Open(true);
            }
        }
    }
}
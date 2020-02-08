package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class NeowRoomPatches
{
//    @SpirePatch(clz = NeowRoom.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {boolean.class})
//    public static class NeowRoom_Constructor
//    {
//        @SpirePostfixPatch
//        public static void Prefix(NeowRoom __instance, boolean isDone)
//        {
//            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && GR.Animator.Dungeon.Series.isEmpty())
//            {
//                GR.UI.SeriesSelection.Open(true);
//            }
//        }
//    }

    @SpirePatch(clz = AbstractEvent.class, method = "onEnterRoom")
    public static class AbstractEvent_OnEnterRoom
    {
        @SpirePostfixPatch
        public static void Prefix(AbstractEvent __instance)
        {
            // TODO: Series Selection Screen, also consider adding some class specific relics to the animator pool
            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && GR.Animator.Dungeon.Series.isEmpty())
            {
                GR.UI.SeriesSelection.Open(true);
            }
        }
    }
}
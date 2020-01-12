package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.neow.NeowRoom;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

public class NeowRoomPatches
{
    @SpirePatch(clz = NeowRoom.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {boolean.class})
    public static class NeowRoom_Constructor
    {
        @SpirePostfixPatch
        public static void Prefix(NeowRoom __instance, boolean isDone)
        {
            JavaUtilities.Log(NeowRoom.class, "On ENTER ROOM:");
            if (!isDone)
            {
                GR.Screens.SeriesSelection.Open(true);
            }
        }
    }
}
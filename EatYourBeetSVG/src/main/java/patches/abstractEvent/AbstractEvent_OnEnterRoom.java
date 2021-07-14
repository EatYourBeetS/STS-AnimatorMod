package patches.abstractEvent;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = AbstractEvent.class, method = "onEnterRoom")
public class AbstractEvent_OnEnterRoom
{
    @SpirePostfixPatch
    public static void Prefix(AbstractEvent __instance)
    {
        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && GR.Animator.Dungeon.Loadouts.isEmpty())
        {
            GR.UI.SeriesSelection.Open(true);
        }
    }
}
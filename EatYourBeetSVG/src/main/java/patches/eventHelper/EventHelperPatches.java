package patches.eventHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.WindingHalls;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class EventHelperPatches
{
    @SpirePatch(clz = EventHelper.class, method = "getEvent", paramtypez = {String.class})
    public static class EventHelper_GetEvent
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractEvent> Prefix(String key)
        {
            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass) && key.equals(WindingHalls.ID))
            {
                return SpireReturn.Return(new eatyourbeets.events.replacement.WindingHalls());
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = EventHelper.class, method = "roll", paramtypez = {Random.class})
    public static class EventHelper_Roll
    {
        @SpirePrefixPatch
        public static SpireReturn<EventHelper.RoomResult> Prefix(Random rng)
        {
            if (UnnamedReignRelic.IsEquipped())
            {
                return SpireReturn.Return(EventHelper.RoomResult.EVENT);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }
}
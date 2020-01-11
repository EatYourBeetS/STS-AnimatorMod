package patches.eventHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;

@SpirePatch(clz = EventHelper.class, method = "roll", paramtypez = {Random.class})
public class EventHelper_Roll
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
package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.relics.UnnamedReignRelic;

public class EventHelperPatches
{
    @SpirePatch(clz = EventHelper.class, method = "roll", paramtypez = {Random.class})
    public static class AbstractDungeonPatches_InitializeCardPools
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
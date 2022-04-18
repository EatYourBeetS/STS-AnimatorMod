package patches.eventHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.beyond.WindingHalls;
import com.megacrit.cardcrawl.events.city.*;
import com.megacrit.cardcrawl.events.exordium.Sssserpent;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.events.replacement.*;
import eatyourbeets.relics.animator.unnamedReign.UnnamedReignRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class EventHelperPatches
{
    public static AbstractEvent GetReplacement(String cardID)
    {
        switch (cardID)
        {
            case GremlinWheelGame.ID: return new AnimatorGremlinWheelGame();
            case ForgottenAltar.ID: return new AnimatorForgottenAltar();
            case WindingHalls.ID: return new AnimatorWindingHalls();
            case Sssserpent.ID: return new AnimatorSsssserpent();
            case DrugDealer.ID: return new AnimatorDrugDealer();
            case Vampires.ID: return new AnimatorVampires();
            case Ghosts.ID: return new AnimatorGhosts();
            case Nest.ID: return new AnimatorNest();

            default: return null;
        }
    }

    @SpirePatch(clz = EventHelper.class, method = "getEvent", paramtypez = {String.class})
    public static class EventHelper_GetEvent
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractEvent> Prefix(String key)
        {
            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
            {
                final AbstractEvent event = GetReplacement(key);
                if (event != null)
                {
                    return SpireReturn.Return(event);
                }
            }

            return SpireReturn.Continue();
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
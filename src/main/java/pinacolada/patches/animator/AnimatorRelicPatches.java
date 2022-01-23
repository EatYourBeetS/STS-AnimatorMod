package pinacolada.patches.animator;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.relics.animator.*;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashSet;

public class AnimatorRelicPatches
{
    public static final HashSet<String> FOOL_COMPATIBLE_RELICS = new HashSet<>();
    public static final HashSet<String> OTHER_COMPATIBLE_RELICS = new HashSet<>();

    static {
        FOOL_COMPATIBLE_RELICS.add(AlchemistGlove.ID);
        FOOL_COMPATIBLE_RELICS.add(BattleDrones.ID);
        FOOL_COMPATIBLE_RELICS.add(CrumblingOrb.ID);
        FOOL_COMPATIBLE_RELICS.add(HeavyHalberd.ID);
        FOOL_COMPATIBLE_RELICS.add(HolyGrailRelic.ID);
        FOOL_COMPATIBLE_RELICS.add(OldCoffin.ID);
        FOOL_COMPATIBLE_RELICS.add(RacePiece.ID);
        OTHER_COMPATIBLE_RELICS.add(Buoy.ID);
        OTHER_COMPATIBLE_RELICS.add(Hoodie.ID);
        OTHER_COMPATIBLE_RELICS.add(ModelSpaceship.ID);
        OTHER_COMPATIBLE_RELICS.add(ShionDessert.ID);
        OTHER_COMPATIBLE_RELICS.add(WornHelmet.ID);
    }

    @SpirePatch(clz = AnimatorRelic.class, method = "canSpawn")
    public static class AnimatorRelicPatches_CanSpawn
    {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AnimatorRelic __instance)
        {
            if ((PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass) && (FOOL_COMPATIBLE_RELICS.contains(__instance.relicId) || OTHER_COMPATIBLE_RELICS.contains(__instance.relicId)))
            || (GR.PCL.Config.EnableRelicsForOtherCharacters.Get() && OTHER_COMPATIBLE_RELICS.contains(__instance.relicId)))
            {
                return SpireReturn.Return(true);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

}

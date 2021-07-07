package patches.orbs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;

public class AbstractOrbPatches
{
    @SpirePatch(clz = AbstractOrb.class, method = "applyFocus")
    public static class AbstractOrbPatches_ApplyFocus
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractOrb __instance)
        {
            AbstractOrbPatches.ApplyAmountChangeToOrb(__instance);
        }
    }

    public static void ApplyAmountChangeToOrb(AbstractOrb __instance)
    {
        String orbType = __instance.ID;

        __instance.passiveAmount += CombatStats.GetAmountIncreasedOnOrb(orbType);
    }
}

package patches.orbs;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ElementalMasteryPower;
import eatyourbeets.utilities.GameUtilities;

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
        ElementalMasteryPower power = GameUtilities.GetPower(AbstractDungeon.player, ElementalMasteryPower.POWER_ID);
        if (power != null) {
            __instance.passiveAmount *= power.percentage;
            __instance.evokeAmount *= power.percentage;
        }
    }
}

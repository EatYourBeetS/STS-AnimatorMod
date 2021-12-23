package pinacolada.patches.abstractOrb;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.LockOnPower;
import pinacolada.actions.orbs.DarkOrbEvokeAction;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class AbstractOrbPatches
{
    @SpirePatch(clz = AbstractOrb.class, method = "applyFocus")
    public static class AbstractOrbPatches_ApplyFocus
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractOrb __instance)
        {
            PCLCombatStats.OnOrbApplyFocus(__instance);
        }
    }

    @SpirePatch(clz = AbstractOrb.class, method = "applyLockOn", paramtypez = {AbstractCreature.class, int.class})
    public static class AbstractOrbPatches_ApplyLockOn
    {
        @SpirePostfixPatch
        public static int Postfix(int retVal, AbstractCreature target, int dmg)
        {
            // This needs to be hardcoded so that Lock-On bonuses affect the vanilla Lock-On power as well
            if (PCLGameUtilities.GetPowerAmount(target, LockOnPower.POWER_ID) >= 1)
            {
                int modifier = PCLCombatStats.GetEffectBonus(LockOnPower.POWER_ID);
                retVal = modifier > 0 ? (int) (dmg * ((retVal / dmg) + modifier)) : retVal;
            }
            return PCLCombatStats.OnOrbApplyLockOn(retVal, target, dmg);
        }
    }


    @SpirePatch(clz = Dark.class, method = "applyFocus")
    public static class DarkPatches_ApplyFocus
    {
        @SpirePostfixPatch
        public static void Postfix(Dark __instance)
        {
            PCLCombatStats.OnOrbApplyFocus(__instance);
        }
    }

    @SpirePatch(clz = Dark.class, method = "onEvoke")
    public static class DarkPatches_OnEvoke
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Dark __instance)
        {
            PCLActions.Top.Add(new DarkOrbEvokeAction(__instance.evokeAmount));
            return SpireReturn.Return(null);
        }
    }
}
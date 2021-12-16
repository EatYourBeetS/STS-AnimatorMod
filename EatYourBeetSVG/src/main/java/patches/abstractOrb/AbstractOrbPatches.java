package patches.abstractOrb;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DarkOrbEvokeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.AnimatorLockOnPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class AbstractOrbPatches
{
    @SpirePatch(clz = Dark.class, method = "onEndOfTurn")
    @SpirePatch(clz = Frost.class, method = "onEndOfTurn")
    @SpirePatch(clz = Lightning.class, method = "onEndOfTurn")
    public static class AbstractOrb_onEndOfTurn
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractOrb __instance)
        {
            if (!(__instance instanceof EmptyOrbSlot))
            {
                CombatStats.OnOrbPassiveEffect(__instance);
            }
        }
    }

    @SpirePatch(clz = Plasma.class, method = "onStartOfTurn")
    public static class AbstractOrb_onStartOfTurn
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractOrb __instance)
        {
            if (!(__instance instanceof EmptyOrbSlot))
            {
                CombatStats.OnOrbPassiveEffect(__instance);
            }
        }
    }

    @SpirePatch(clz = AbstractOrb.class, method = "applyFocus")
    public static class AbstractOrbPatches_ApplyFocus
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractOrb __instance)
        {
            CombatStats.OnOrbApplyFocus(__instance);
        }
    }

    @SpirePatch(clz = AbstractOrb.class, method = "applyLockOn", paramtypez = {AbstractCreature.class, int.class})
    public static class AbstractOrbPatches_ApplyLockOn
    {
        @SpirePostfixPatch
        public static int Postfix(int retVal, AbstractCreature target, int dmg)
        {
            // This needs to be hardcoded so that Lock-On bonuses affect the vanilla Lock-On power as well
            if (GameUtilities.GetPowerAmount(target, LockOnPower.POWER_ID) >= 1)
            {
                retVal = AnimatorLockOnPower.ENEMY_MODIFIER > 0 ? (int) (dmg * ((retVal / dmg) + AnimatorLockOnPower.ENEMY_MODIFIER)) : retVal;
            }
            return CombatStats.OnOrbApplyLockOn(retVal, target, dmg);
        }
    }


    @SpirePatch(clz = Dark.class, method = "applyFocus")
    public static class DarkPatches_ApplyFocus
    {
        @SpirePostfixPatch
        public static void Postfix(Dark __instance)
        {
            CombatStats.OnOrbApplyFocus(__instance);
        }
    }

    @SpirePatch(clz = Dark.class, method = "onEvoke")
    public static class DarkPatches_OnEvoke
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Dark __instance)
        {
            AbstractDungeon.actionManager.addToTop(new DarkOrbEvokeAction(new DamageInfo(AbstractDungeon.player, __instance.evokeAmount, DamageInfo.DamageType.THORNS), GR.Enums.AttackEffect.DARK));
            return SpireReturn.Return(null);
        }
    }
}
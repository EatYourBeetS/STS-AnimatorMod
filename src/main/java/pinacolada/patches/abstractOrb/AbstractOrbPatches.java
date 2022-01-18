package pinacolada.patches.abstractOrb;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.ElectroPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.actions.orbs.DarkOrbEvokeAction;
import pinacolada.actions.orbs.LightningOrbAction;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;

public class AbstractOrbPatches
{
    protected static final FieldInfo<Float> _fontScale = PCLJUtils.GetField("fontScale", AbstractOrb.class);
    protected static final HashMap<String, OrbStrings> orbStrings = new HashMap<>();
    protected static final HashMap<String, PCLCardTooltip> tooltips = new HashMap<>();

    protected static OrbStrings GetOrbStrings(String orbID) {
        return orbStrings.getOrDefault(orbID, GR.GetOrbStrings(GR.PCL.CreateID(orbID)));
    }

    protected static PCLCardTooltip GetTooltip(AbstractOrb orb) {
        OrbStrings orbStrings = GetOrbStrings(orb.ID);
        return tooltips.getOrDefault(orb.ID, new PCLCardTooltip(orbStrings.NAME, PCLJUtils.Format(orbStrings.DESCRIPTION[0], orb.passiveAmount, orb.evokeAmount)));
    }

    @SpirePatch(clz = AbstractOrb.class, method = "applyFocus")
    public static class AbstractOrbPatches_ApplyFocus
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractOrb __instance)
        {
            // Certain mods may instantiate Orb instances in static methods before CombatStats is initialized, which will cause this method call to crash without this in-game check
            if (PCLGameUtilities.InGame()) {
                PCLCombatStats.OnOrbApplyFocus(__instance);
            }

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
                retVal = modifier > 0 ? (dmg * ((retVal / dmg) + modifier)) : retVal;
            }
            return PCLCombatStats.OnOrbApplyLockOn(retVal, target, dmg);
        }
    }

    @SpirePatch(clz = AbstractOrb.class, method = "update")
    public static class AbstractOrbPatches_update
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractOrb __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass) && PCLGameUtilities.IsValidOrb(__instance)) {
                __instance.hb.update();
                if (__instance.hb.hovered)
                {
                    PCLCardTooltip.QueueTooltip(GetTooltip(__instance), InputHelper.mX + __instance.hb.width, InputHelper.mY + (__instance.hb.height * 0.5f));
                }
                _fontScale.Set(__instance, MathHelper.scaleLerpSnap(_fontScale.Get(__instance), 0.7F));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Dark.class, method = "applyFocus")
    public static class DarkPatches_ApplyFocus
    {
        @SpirePostfixPatch
        public static void Postfix(Dark __instance)
        {
            if (PCLGameUtilities.InGame()) {
                PCLCombatStats.OnOrbApplyFocus(__instance);
            }
        }
    }

    @SpirePatch(clz = Dark.class, method = "onEvoke")
    public static class DarkPatches_OnEvoke
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Dark __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                PCLActions.Top.Add(new DarkOrbEvokeAction(__instance.evokeAmount));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Dark.class, method = "updateDescription")
    public static class DarkPatches_UpdateDescription
    {
        @SpirePostfixPatch
        public static void Postfix(Dark __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                __instance.description = PCLJUtils.Format(GetOrbStrings(__instance.ID).DESCRIPTION[0], __instance.passiveAmount, __instance.evokeAmount);
            }
        }
    }

    @SpirePatch(clz = Frost.class, method = "updateDescription")
    public static class FrostPatches_UpdateDescription
    {
        @SpirePostfixPatch
        public static void Postfix(Frost __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                __instance.description = PCLJUtils.Format(GetOrbStrings(__instance.ID).DESCRIPTION[0], __instance.passiveAmount, __instance.evokeAmount);
            }
        }
    }

    @SpirePatch(clz = Lightning.class, method = "onEvoke")
    public static class LightningPatches_OnEvoke
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Lightning __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                PCLActions.Top.Add(new LightningOrbAction(null, __instance.evokeAmount, AbstractDungeon.player.hasPower(ElectroPower.POWER_ID)));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Lightning.class, method = "onEndOfTurn")
    public static class LightningPatches_OnEndOfTurn
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Lightning __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                PCLActions.Top.Add(new LightningOrbAction(__instance, __instance.passiveAmount, AbstractDungeon.player.hasPower(ElectroPower.POWER_ID)));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Lightning.class, method = "triggerPassiveEffect")
    public static class LightningPatches_TriggerPassiveEffect
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(Lightning __instance, DamageInfo info, boolean hitAll)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                PCLActions.Top.Add(new LightningOrbAction(__instance, info.output, hitAll));
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Lightning.class, method = "updateDescription")
    public static class LightningPatches_UpdateDescription
    {
        @SpirePostfixPatch
        public static void Postfix(Lightning __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                __instance.description = PCLJUtils.Format(GetOrbStrings(__instance.ID).DESCRIPTION[0], __instance.passiveAmount, __instance.evokeAmount);
            }
        }
    }

    @SpirePatch(clz = Plasma.class, method = "updateDescription")
    public static class PlasmaPatches_UpdateDescription
    {
        @SpirePostfixPatch
        public static void Postfix(Plasma __instance)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass)) {
                __instance.description = PCLJUtils.Format(GetOrbStrings(__instance.ID).DESCRIPTION[0], __instance.passiveAmount, __instance.evokeAmount);
            }
        }
    }
}
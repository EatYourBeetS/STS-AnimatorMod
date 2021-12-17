package pinacolada.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.powers.replacement.*;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

@SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez =
{AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class})
public class ApplyPowerAction_Ctor
{
    private static final FieldInfo<AbstractPower> _powerToApply = PCLJUtils.GetField("powerToApply", ApplyPowerAction.class);
    private static final FieldInfo<Boolean> _justAppliedForVulnerable = PCLJUtils.GetField("justApplied", VulnerablePower.class);
    private static final FieldInfo<Boolean> _justAppliedForWeak = PCLJUtils.GetField("justApplied", WeakPower.class);

    @SpirePrefixPatch
    public static void Prefix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, @ByRef AbstractPower[] powerToApply,
                               int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        AbstractPower power = powerToApply[0];
        if (power instanceof ConstrictedPower && !(power instanceof PCLConstrictedPower))
        {
            powerToApply[0] = new PCLConstrictedPower(power.owner, ((ConstrictedPower)power).source, power.amount);
            _powerToApply.Set(__instance, powerToApply[0]);
        }

        if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass))
        {
            if (power instanceof VulnerablePower && !(power instanceof PCLVulnerablePower))
            {
                boolean justApplied = _justAppliedForVulnerable.Get(power);
                powerToApply[0] = new PCLVulnerablePower(power.owner, power.amount, justApplied);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof WeakPower && !(power instanceof PCLWeakPower))
            {
                boolean justApplied = _justAppliedForWeak.Get(power);
                powerToApply[0] = new PCLWeakPower(power.owner, power.amount, justApplied);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof FrailPower && !(power instanceof PCLFrailPower))
            {
                powerToApply[0] = new PCLFrailPower(power.owner, power.amount, !PCLGameUtilities.IsPlayer(source));
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof LockOnPower && !(power instanceof PCLLockOnPower))
            {
                powerToApply[0] = new PCLLockOnPower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof BlurPower && !(power instanceof PCLBlurPower))
            {
                powerToApply[0] = new PCLBlurPower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof PlatedArmorPower && !(power instanceof PCLPlatedArmorPower))
            {
                powerToApply[0] = new PCLPlatedArmorPower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof MetallicizePower && !(power instanceof PCLMetallicizePower))
            {
                powerToApply[0] = new PCLMetallicizePower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
        }
    }

    @SpirePostfixPatch
    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source,
                               AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        if (!PCLGameUtilities.CanApplyPower(source, target, powerToApply, __instance))
        {
            __instance.isDone = true;
        }
    }
}

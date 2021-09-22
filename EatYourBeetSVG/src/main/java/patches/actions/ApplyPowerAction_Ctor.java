package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.replacement.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

@SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez =
{AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class})
public class ApplyPowerAction_Ctor
{
    private static final FieldInfo<AbstractPower> _powerToApply = JUtils.GetField("powerToApply", ApplyPowerAction.class);

    @SpirePrefixPatch
    public static void Prefix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, @ByRef AbstractPower[] powerToApply,
                               int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        AbstractPower power = powerToApply[0];
        if (power instanceof ConstrictedPower && !(power instanceof AnimatorConstrictedPower))
        {
            powerToApply[0] = new AnimatorConstrictedPower(power.owner, ((ConstrictedPower)power).source, power.amount);
            _powerToApply.Set(__instance, powerToApply[0]);
        }

        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
        {
            if (power instanceof VulnerablePower && !(power instanceof AnimatorVulnerablePower))
            {
                powerToApply[0] = new AnimatorVulnerablePower(power.owner, power.amount, !GameUtilities.IsPlayer(source));
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof WeakPower && !(power instanceof AnimatorWeakPower))
            {
                powerToApply[0] = new AnimatorWeakPower(power.owner, power.amount, !GameUtilities.IsPlayer(source));
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof FrailPower && !(power instanceof AnimatorFrailPower))
            {
                powerToApply[0] = new AnimatorFrailPower(power.owner, power.amount, !GameUtilities.IsPlayer(source));
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof LockOnPower && !(power instanceof AnimatorLockOnPower))
            {
                powerToApply[0] = new AnimatorLockOnPower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof BlurPower && !(power instanceof AnimatorBlurPower))
            {
                powerToApply[0] = new AnimatorBlurPower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof PlatedArmorPower && !(power instanceof AnimatorPlatedArmorPower))
            {
                powerToApply[0] = new AnimatorPlatedArmorPower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            if (power instanceof MetallicizePower && !(power instanceof AnimatorMetallicizePower))
            {
                powerToApply[0] = new AnimatorMetallicizePower(power.owner, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
        }
    }

    @SpirePostfixPatch
    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source,
                               AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        if (!GameUtilities.CanApplyPower(source, target, powerToApply, __instance))
        {
            __instance.isDone = true;
        }
    }
}

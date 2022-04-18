package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.*;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import javassist.CtBehavior;

public class ApplyPowerActionPatches
{
    public static final FieldInfo<AbstractPower> _powerToApply = JUtils.GetField("powerToApply", ApplyPowerAction.class);

    @SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez =
            {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class})
    public static class ApplyPowerAction_Ctor
    {
        @SpirePrefixPatch
        public static void Prefix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, @ByRef AbstractPower[] powerToApply,
                                  int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
        {
            final AbstractPower power = powerToApply[0];
            if (power instanceof ConstrictedPower && !(power instanceof AnimatorConstrictedPower))
            {
                powerToApply[0] = new AnimatorConstrictedPower(power.owner, ((ConstrictedPower) power).source, power.amount);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            else if (power instanceof VulnerablePower && !(power instanceof AnimatorVulnerablePower))
            {
                powerToApply[0] = new AnimatorVulnerablePower(power.owner, power.amount, source == null || !source.isPlayer);
                _powerToApply.Set(__instance, powerToApply[0]);
            }
            else if (GameUtilities.IsEYBPlayerClass())
            {
                if (power instanceof BlurPower && !(power instanceof AnimatorBlurPower))
                {
                    powerToApply[0] = new AnimatorBlurPower(power.owner, power.amount);
                    _powerToApply.Set(__instance, powerToApply[0]);
                }
                else if (power instanceof PlatedArmorPower && !(power instanceof AnimatorPlatedArmorPower))
                {
                    powerToApply[0] = new AnimatorPlatedArmorPower(power.owner, power.amount);
                    _powerToApply.Set(__instance, powerToApply[0]);
                }
                else if (power instanceof MetallicizePower && !(power instanceof AnimatorMetallicizePower))
                {
                    powerToApply[0] = new AnimatorMetallicizePower(power.owner, power.amount);
                    _powerToApply.Set(__instance, powerToApply[0]);
                }
                else if (GameUtilities.IsPlayer(target) && power instanceof IntangiblePlayerPower && !(power instanceof AnimatorIntangiblePower))
                {
                    powerToApply[0] = new AnimatorIntangiblePower(power.owner, power.amount);
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

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ApplyPowerAction_Update
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(ApplyPowerAction __instance)
        {
            CombatStats.OnApplyPower(__instance.source, __instance.target, _powerToApply.Get(__instance));
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                //    ...
                //    this.target.getPower("Artifact").onSpecificTrigger();
                //    return;
                // }
                //
                // AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
                final Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "effectList");
                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0]};
            }
        }
    }
}
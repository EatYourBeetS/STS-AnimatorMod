package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez =
{AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class})
public class ApplyPowerAction_Ctor
{
    @SpirePostfixPatch
    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source,
                               AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect)
    {
        if (!GameUtilities.CanApplyPower(source, target, powerToApply))
        {
            __instance.isDone = true;
        }
    }
}

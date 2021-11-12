package patches.gameActionManager;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.powers.animator.MindControlPower;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

// Make the enemy hit itself (Also VampireDamageAction because it obviously does not inherit from DamageAction)
public class EnemyActionPatches
{
    @SpirePatch(clz = DamageAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCreature.class, DamageInfo.class, AbstractGameAction.AttackEffect.class})
    public static class DamageAction_ctor
    {
        @SpirePostfixPatch
        public static void Method(DamageAction action, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect)
        {
            if (action != null && action.source != null)
            {
                for (AbstractPower power: action.source.powers) {
                    if (power instanceof GeassPower && ((GeassPower) power).enabled) {
                        info.applyPowers(action.source, action.source);
                        action.target = action.source;
                    }
                    else if (power instanceof MindControlPower && ((MindControlPower) power).active && ((MindControlPower) power).canRedirect) {
                        AbstractCreature newT = GameUtilities.GetRandomEnemy(true);
                        if (newT == null) {
                            newT = action.source;
                        }
                        info.applyPowers(action.source, newT);
                        action.target = newT;
                    }
                }

                for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                    boolean isIntercepted = JUtils.Find(mo.powers, po -> po instanceof MindControlPower && ((MindControlPower) po).canIntercept && ((MindControlPower) po).active) != null;
                    if (isIntercepted) {
                        info.applyPowers(action.source, mo);
                        action.target = mo;
                        break;
                    }
                }
            }
        }
    }

    @SpirePatch(clz = VampireDamageAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCreature.class, DamageInfo.class, AbstractGameAction.AttackEffect.class})
    public static class VampireDamageAction_ctor
    {
        @SpirePostfixPatch
        public static void Method(VampireDamageAction action, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect)
        {
            if (action != null && action.source != null)
            {
                for (AbstractPower power: action.source.powers) {
                    if (power instanceof GeassPower && ((GeassPower) power).enabled) {
                        info.applyPowers(action.source, action.source);
                        action.target = action.source;
                    }
                    else if (power instanceof MindControlPower && ((MindControlPower) power).active && ((MindControlPower) power).canRedirect) {
                        AbstractCreature newT = GameUtilities.GetRandomEnemy(true);
                        if (newT == null) {
                            newT = action.source;
                        }
                        info.applyPowers(action.source, newT);
                        action.target = newT;
                    }
                }

                for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                    boolean isIntercepted = JUtils.Find(mo.powers, po -> po instanceof MindControlPower && ((MindControlPower) po).canIntercept && ((MindControlPower) po).active) != null;
                    if (isIntercepted) {
                        info.applyPowers(action.source, mo);
                        action.target = mo;
                        break;
                    }
                }
            }
        }
    }
}
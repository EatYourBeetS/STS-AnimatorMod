package pinacolada.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.powers.PCLCombatStats;

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
                PCLCombatStats.OnDamageAction(action, target, info, effect);
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
                PCLCombatStats.OnDamageAction(action, target, info, effect);
            }
        }
    }

    // This allows block-modifying powers like Frail and Dexterity to affect enemies
    @SpirePatch(clz = GainBlockAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCreature.class, int.class})
    public static class GainBlockAction_ctor
    {
        @SpirePostfixPatch
        public static void Method(GainBlockAction action, AbstractCreature target, int amount)
        {
            ModifyBlock(action, amount);
        }
    }

    @SpirePatch(clz = GainBlockAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCreature.class, AbstractCreature.class, int.class})
    public static class GainBlockAction_ctor2
    {
        @SpirePostfixPatch
        public static void Method(GainBlockAction action, AbstractCreature target, AbstractCreature source, int amount)
        {
            ModifyBlock(action, amount);
        }
    }

    private static void ModifyBlock(GainBlockAction action, int amount) {
        if (action != null && action.source instanceof AbstractMonster)
        {
            float tmp = action.amount;
            for (AbstractPower power: action.source.powers) {
                tmp = power.modifyBlock(tmp);
            }
            action.amount = (int)tmp;
        }
    }
}
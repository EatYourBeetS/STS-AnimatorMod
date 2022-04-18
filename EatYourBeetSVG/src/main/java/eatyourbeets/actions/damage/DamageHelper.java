package eatyourbeets.actions.damage;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class DamageHelper
{
    public static void ApplyTint(AbstractCreature target, Color overrideColor, AbstractGameAction.AttackEffect attackEffect)
    {
        final Color tint = overrideColor != null ? overrideColor : AttackEffects.GetDamageTint(attackEffect);
        if (target != null && tint != null)
        {
            target.tint.color.set(tint.cpy());
            target.tint.changeColor(Color.WHITE.cpy());
        }
    }

    public static void DealDamage(AbstractCreature target, DamageInfo info, boolean bypassBlock, boolean bypassThorns, boolean canKill)
    {
        if (!GameUtilities.IsValidTarget(target))
        {
            return;
        }

        int previousBlock = 0;
        if (bypassBlock)
        {
            previousBlock = target.currentBlock;
            target.currentBlock = 0;
        }

        ArrayList<AbstractPower> ignoredPowers = null;
        if (bypassThorns)
        {
            ignoredPowers = RemovePowers(target);
        }

        final int totalHP = GameUtilities.GetHP(target, true, true);
        if (!canKill && info.output >= totalHP)
        {
            info.output = Math.min(info.output, totalHP - 1);
        }

        target.damage(info);

        if (!GameUtilities.IsDeadOrEscaped(target))
        {
            if (ignoredPowers != null)
            {
                ReapplyPowers(ignoredPowers, target);
            }

            if (previousBlock > 0)
            {
                target.currentBlock = previousBlock;
            }
        }
    }

    public static ArrayList<AbstractPower> RemovePowers(AbstractCreature target)
    {
        ArrayList<AbstractPower> toReAdd = new ArrayList<>();

        AddIgnoredPower(toReAdd, target, ThornsPower.POWER_ID);
        AddIgnoredPower(toReAdd, target, EarthenThornsPower.POWER_ID);
        AddIgnoredPower(toReAdd, target, MalleablePower.POWER_ID);
        AddIgnoredPower(toReAdd, target, FlameBarrierPower.POWER_ID);
        AddIgnoredPower(toReAdd, target, CurlUpPower.POWER_ID);

        for (int i = target.powers.size()-1; i >= 0; i--)
        {
            if (target.powers.get(i).ID.toLowerCase().contains("thorns"))
            {
                toReAdd.add(target.powers.get(i));
                target.powers.remove(i);
            }
        }

        return toReAdd;
    }

    public static void ReapplyPowers(ArrayList<AbstractPower> powers, AbstractCreature target)
    {
        for (AbstractPower p : powers)
        {
            AbstractPower current = target.getPower(p.ID);
            if (current != null)
            {
                current.amount += p.amount;
            }
            else
            {
                target.powers.add(p);
            }
        }
    }

    public static void AddIgnoredPower(ArrayList<AbstractPower> powers, AbstractCreature target, String powerID)
    {
        AbstractPower power = target.getPower(powerID);
        if (power != null)
        {
            powers.add(power);
            target.powers.remove(power);
        }
    }
}

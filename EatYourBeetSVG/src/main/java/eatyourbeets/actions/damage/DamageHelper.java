package eatyourbeets.actions.damage;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class DamageHelper
{
    public static void DealDamage(AbstractCreature target, DamageInfo info, boolean bypassBlock, boolean bypassThorns)
    {
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
        ArrayList<AbstractPower> powers = new ArrayList<>();

        AddIgnoredPower(powers, target, ThornsPower.POWER_ID);
        AddIgnoredPower(powers, target, EarthenThornsPower.POWER_ID);
        AddIgnoredPower(powers, target, MalleablePower.POWER_ID);
        AddIgnoredPower(powers, target, FlameBarrierPower.POWER_ID);
        AddIgnoredPower(powers, target, CurlUpPower.POWER_ID);
        AddIgnoredPower(powers, target, "infinitespire:TempThorns");

        return powers;
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
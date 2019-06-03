package eatyourbeets.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;

import java.util.ArrayList;

public class PridePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(PridePower.class.getSimpleName());

    public PridePower(AbstractPlayer owner)
    {
        super(owner, POWER_ID);

        this.priority = -99;

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        damageAmount = super.onAttacked(info, damageAmount);

        AbstractPlayer p = AbstractDungeon.player;
        if (damageAmount > 0)
        {
            ArrayList<Dark> darkOrbs = new ArrayList<>();
            for (AbstractOrb orb : p.orbs)
            {
                Dark dark = Utilities.SafeCast(orb, Dark.class);
                if (dark != null)
                {
                    darkOrbs.add(dark);
                }
            }

            if (darkOrbs.size() > 0)
            {
                damageAmount = AbsorbDamage(damageAmount, darkOrbs);
            }
        }

        return damageAmount;
    }

    @Override
    public void atStartOfTurn()
    {
        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));

        super.atStartOfTurn();
    }

    private int AbsorbDamage(int damage, ArrayList<Dark> darkOrbs)
    {
        int max = Integer.MIN_VALUE;

        Dark best = darkOrbs.get(0);
//        Dark best = null;
//
//        for (Dark dark : darkOrbs)
//        {
//            if (dark.evokeAmount > max)
//            {
//                max = dark.evokeAmount;
//                best = dark;
//            }
//        }

        if (best != null)
        {
            float temp = damage;

            damage -= best.evokeAmount;
            best.evokeAmount -= temp;

            if (best.evokeAmount <= 0)
            {
                darkOrbs.remove(best);
                best.evokeAmount = 0;
                GameActionsHelper.AddToTop(new EvokeSpecificOrbAction(best));
            }

            if (damage > 0 && darkOrbs.size() > 0)
            {
                return AbsorbDamage(damage, darkOrbs);
            }
        }

        return damage;
    }
}

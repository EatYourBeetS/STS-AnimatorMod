package eatyourbeets.powers.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class PridePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(PridePower.class);

    public PridePower(AbstractPlayer owner)
    {
        super(owner, POWER_ID);

        this.priority = -99;

        Initialize(-1);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (damageAmount > 0)
        {
            ArrayList<Dark> darkOrbs = new ArrayList<>();
            for (AbstractOrb orb : player.orbs)
            {
                Dark dark = JUtils.SafeCast(orb, Dark.class);
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

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        RemovePower();
    }

    private int AbsorbDamage(int damage, ArrayList<Dark> darkOrbs)
    {
        int max = Integer.MIN_VALUE;

        Dark next = darkOrbs.get(0);

        if (next != null)
        {
            float temp = damage;

            damage -= next.evokeAmount;
            next.evokeAmount -= temp;

            if (next.evokeAmount <= 0)
            {
                darkOrbs.remove(next);
                next.evokeAmount = 0;
                GameActions.Top.Add(new EvokeSpecificOrbAction(next));
            }

            if (damage > 0 && darkOrbs.size() > 0)
            {
                return AbsorbDamage(damage, darkOrbs);
            }
        }

        return damage;
    }
}

package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.JavaUtilities;

public class PoisonAffinityPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(PoisonAffinityPower.class);

    public PoisonAffinityPower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);

        this.amount = stacks;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (source != null && source.isPlayer)
        {
            if (power.ID.equals(PoisonPower.POWER_ID))
            {
                power.amount += this.amount;

                AbstractGameAction action = AbstractDungeon.actionManager.currentAction;
                if (action instanceof ApplyPower || action instanceof ApplyPowerAction)
                {
                    action.amount += this.amount;
                }
                else
                {
                    JavaUtilities.GetLogger(this).warn("Unknown action type: " + action.getClass().getName());
                }
            }
        }

        super.onApplyPower(power, target, source);
    }
}

package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class ReducePower extends EYBActionWithCallback<AbstractPower>
{
    private String powerID;
    private AbstractPower power;

    public ReducePower(AbstractCreature target, AbstractCreature source, String powerID, int amount)
    {
        super(ActionType.REDUCE_POWER, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.powerID = powerID;

        Initialize(target, source, amount);
    }

    public ReducePower(AbstractCreature target, AbstractCreature source, AbstractPower power, int amount)
    {
        super(ActionType.REDUCE_POWER, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.power = power;

        Initialize(target, source, amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.powerID != null)
        {
            power = this.target.getPower(this.powerID);
        }

        if (power != null)
        {
            if (power.canGoNegative && power.amount < 0)
            {
                power.stackPower(amount);
            }
            else
            {
                power.reducePower(amount);
            }

            power.updateDescription();
            AbstractDungeon.onModifyPower();

            final EYBPower p = JUtils.SafeCast(power, EYBPower.class);
            if (power.amount == 0 && (p == null || !p.canBeZero))
            {
                GameActions.Top.RemovePower(source, target, power);
            }
        }
    }
}

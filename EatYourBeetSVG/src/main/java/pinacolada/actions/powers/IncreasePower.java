package pinacolada.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.utilities.PCLActions;

public class IncreasePower extends EYBActionWithCallback<AbstractPower>
{
    private String powerID;
    private AbstractPower power;

    public IncreasePower(AbstractCreature target, AbstractCreature source, String powerID, int amount)
    {
        super(ActionType.REDUCE_POWER, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.actionType = ActionType.REDUCE_POWER;

        Initialize(target, source, amount);
    }

    public IncreasePower(AbstractCreature target, AbstractCreature source, AbstractPower power, int amount)
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
            power.stackPower(amount);
            power.updateDescription();
            AbstractDungeon.onModifyPower();

            if (power.amount == 0)
            {
                PCLActions.Top.RemovePower(source, target, power);
            }
        }
    }
}

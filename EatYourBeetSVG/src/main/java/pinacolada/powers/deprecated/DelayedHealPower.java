package pinacolada.powers.deprecated;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class DelayedHealPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(DelayedHealPower.class);

    public DelayedHealPower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);
        this.amount = stacks;
        this.type = PowerType.BUFF;
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        PCLActions.Bottom.Add(new HealAction(owner, owner, amount));
        RemovePower();
    }
}
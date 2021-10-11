package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class HurricanePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(HurricanePower.class);
    private static final int TRIGGER_MULTIPLIER = 2;

    public HurricanePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, (amount - 1) * TRIGGER_MULTIPLIER);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
    }

    @Override
    public void onRemove()
    {
        this.amount = 0;
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

       GameActions.Bottom.ChannelRandomCommonOrb(rng).AddCallback(orbs -> {
           if (orbs.size() > 0 && amount > 0)
           {
               AbstractOrb orb = orbs.get(0);
               GameActions.Top.TriggerOrbPassive(orb, (amount - 1) * TRIGGER_MULTIPLIER);
           }
       });
    }
}

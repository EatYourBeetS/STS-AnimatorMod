package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class HurricanePower extends AnimatorClickablePower
{
    public static final String POWER_ID = CreateFullID(HurricanePower.class);
    private static final int TRIGGER_MULTIPLIER = 2;

    public HurricanePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID, PowerTriggerConditionType.Energy, 1);

        triggerCondition.SetUses(2, true, false);

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
    }

    @Override
    public String GetUpdatedDescription()
    {
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }

        return this.description = FormatDescription(0, (amount - 1) * TRIGGER_MULTIPLIER);
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
    public void OnUse(AbstractMonster m)
    {
        super.OnUse(m);

       GameActions.Bottom.ChannelRandomOrbs(1).AddCallback(orbs -> {
           if (orbs.size() > 0 && amount > 0)
           {
               AbstractOrb orb = orbs.get(0);
               GameActions.Top.TriggerOrbPassive(orb, (amount - 1) * TRIGGER_MULTIPLIER);
           }
       });
    }
}

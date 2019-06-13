package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.animator.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class HealingWispPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HealingWispPower.class.getSimpleName());

    public HealingWispPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        for (AbstractCreature c : PlayerStatistics.GetAllCharacters(true))
        {
            c.heal(amount, true);
        }
    }
}

package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class StolenGoldPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(StolenGoldPower.class);

    public StolenGoldPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        final AbstractMonster m = JUtils.SafeCast(owner, AbstractMonster.class);
        if (m == null || m.hasPower(MinionPower.POWER_ID) || m.hasPower(RegrowPower.POWER_ID))
        {
            maxAmount = 0;
        }
        else if (m.type == AbstractMonster.EnemyType.BOSS)
        {
            maxAmount = 50;
        }
        else if (m.type == AbstractMonster.EnemyType.ELITE)
        {
            maxAmount = 25;
        }
        else
        {
            maxAmount = 10;
        }

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, (maxAmount - this.amount));
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Top.GainGold(amount);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        int initialGold = this.amount;

        super.stackPower(stackAmount);

        int goldGain = this.amount - initialGold;
        if (goldGain > 0)
        {
            GameActions.Top.GainGold(goldGain);
        }
    }
}

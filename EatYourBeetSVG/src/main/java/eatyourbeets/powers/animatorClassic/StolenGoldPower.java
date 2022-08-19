package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class StolenGoldPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(StolenGoldPower.class);

    private final int goldCap;

    public StolenGoldPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        type = PowerType.DEBUFF;

        AbstractMonster m = (AbstractMonster) owner;
        if (m.hasPower(MinionPower.POWER_ID) || m.hasPower(RegrowPower.POWER_ID))
        {
            goldCap = 0;
        }
        else if (m.type == AbstractMonster.EnemyType.BOSS)
        {
            goldCap = 50;
        }
        else if (m.type == AbstractMonster.EnemyType.ELITE)
        {
            goldCap = 25;
        }
        else
        {
            goldCap = 10;
        }

        this.amount = Math.min(goldCap, amount);

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        this.description = desc[0] + this.amount + desc[1] + (goldCap - this.amount) + desc[2];
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        int goldGain = Math.min(goldCap, amount);
        if (goldGain > 0)
        {
            GameActions.Top.GainGold(goldGain);
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        int initialGold = amount;

        super.stackPower(stackAmount);

        if (this.amount > goldCap)
        {
            this.amount = goldCap;
        }

        int goldGain = this.amount - initialGold;
        if (goldGain > 0)
        {
            GameActions.Top.GainGold(goldGain);
        }
    }
}

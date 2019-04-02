package eatyourbeets.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.HiteiAction;
import eatyourbeets.rewards.SpecialGoldReward;

public class HiteiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HiteiPower.class.getSimpleName());

    private final String originalName;

    private int upgradeStack;
    private int unupgradedStacks;
    private int goldGain;
    private int goldCap = 100;

    public HiteiPower(AbstractPlayer owner, int goldGain, boolean upgraded, String originalName)
    {
        super(owner, POWER_ID);

        if (upgraded)
        {
            this.upgradeStack += 1;
        }
        else
        {
            this.unupgradedStacks = 1;
        }

        this.originalName = originalName;
        this.amount = 0;
        this.goldGain = goldGain;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = (powerStrings.DESCRIPTIONS[0] + goldGain + powerStrings.DESCRIPTIONS[1] + unupgradedStacks + powerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amount = Math.min(goldCap, this.amount + goldGain);

        for (int i = 0; i < unupgradedStacks; i++)
        {
            GameActionsHelper.AddToBottom(new HiteiAction(2));
        }

        for (int i = 0; i < upgradeStack; i++)
        {
            GameActionsHelper.AddToBottom(new HiteiAction(3));
        }

        this.flash();
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        AbstractRoom room = PlayerStatistics.GetCurrentRoom();
        if (room != null && room.rewardAllowed)
        {
            room.rewards.add(0, new SpecialGoldReward(this.originalName, amount));
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        HiteiPower other = Utilities.SafeCast(power, HiteiPower.class);
        if (other != null && power.owner == target)
        {
            int bonus = (60 - (10 * (unupgradedStacks + upgradeStack)));

            if (bonus > 0)
            {
                this.goldCap += bonus;
            }

            this.unupgradedStacks += other.unupgradedStacks;
            this.upgradeStack += other.upgradeStack;

            this.goldGain += other.goldGain;
        }

        super.onApplyPower(power, target, source);
    }
}

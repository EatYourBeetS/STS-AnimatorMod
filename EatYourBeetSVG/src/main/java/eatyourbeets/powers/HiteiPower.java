package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.HiteiAction;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.rewards.SpecialGoldReward;

public class HiteiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HiteiPower.class.getSimpleName());

    private final String originalName;
    private int stacks;
    private int goldGain;
    private int goldCap = 100;

    public HiteiPower(AbstractPlayer owner, int goldGain, String originalName)
    {
        super(owner, POWER_ID);

        this.originalName = originalName;
        this.amount = 0;
        this.goldGain = goldGain;
        this.stacks = 1;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = (powerStrings.DESCRIPTIONS[0] + goldGain + powerStrings.DESCRIPTIONS[1] + stacks + powerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amount = Math.min(goldCap, this.amount + goldGain);

        for (int i = 0; i < stacks; i++)
        {
            GameActionsHelper.AddToBottom(new HiteiAction());
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
            int bonus = (60 - (10 * stacks));

            if (bonus > 0)
            {
                this.goldCap += bonus;
            }

            this.stacks += 1;
            this.goldGain += other.goldGain;
        }

        super.onApplyPower(power, target, source);
    }
}

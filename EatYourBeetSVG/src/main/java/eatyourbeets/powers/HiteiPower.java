package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.rewards.SpecialGoldReward;

public class HiteiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HiteiPower.class.getSimpleName());

    private final AbstractPlayer player;
    private String originalName;
    private int stacks;
    private int exhaustCards;
    private int goldGain;
    private int goldCap = 100;

    public HiteiPower(AbstractPlayer owner, int goldGain, String originalName)
    {
        super(owner, POWER_ID);

        this.originalName = originalName;
        this.player = Utilities.SafeCast(this.owner, AbstractPlayer.class);
        this.amount = 0;
        this.goldGain = goldGain;
        this.exhaustCards = 1;
        this.stacks = 1;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = (powerStrings.DESCRIPTIONS[0] + goldGain + powerStrings.DESCRIPTIONS[1] + exhaustCards + powerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (this.amount < goldCap)
        {
            this.amount += goldGain;
        }

        for (int i = 0; i < exhaustCards; i++)
        {
            AbstractCard card;
            CardGroup group;
            if (player.drawPile.size() > 0)
            {
                group = player.drawPile;
            }
            else if (player.discardPile.size() > 0)
            {
                group = player.discardPile;
            }
            else
            {
                return;
            }

            card = group.getRandomCard(true);
            if (card != null)
            {
                ShowCardBrieflyEffect effect = new ShowCardBrieflyEffect(card, Settings.WIDTH / 3f, Settings.HEIGHT / 2f);

                AbstractDungeon.effectsQueue.add(effect);
                GameActionsHelper.AddToBottom(new WaitAction(effect.duration));
                GameActionsHelper.AddToBottom(new ExhaustSpecificCardAction(card, group, true));
            }
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
            this.goldGain += other.goldGain;
            this.exhaustCards += 1;
            this.goldCap += 100 / (stacks * 3);
            this.stacks += 1;
        }

        super.onApplyPower(power, target, source);
    }
}

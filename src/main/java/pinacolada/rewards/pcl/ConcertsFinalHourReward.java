package pinacolada.rewards.pcl;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardSave;
import pinacolada.cards.base.CardSeries;
import pinacolada.relics.pcl.ConcertsFinalHour;
import pinacolada.resources.GR;
import pinacolada.rewards.PCLReward;
import pinacolada.utilities.PCLJUtils;

public class ConcertsFinalHourReward extends PCLReward
{
    public static final String ID = CreateFullID(ConcertsFinalHourReward.class);

    public final CardSeries series;

    public ConcertsFinalHourReward(CardSeries series)
    {
        super(ImageMaster.REWARD_CARD_NORMAL, GR.GetRelicStrings(ConcertsFinalHour.ID).NAME, GR.Enums.Rewards.SERIES_CARDS);

        this.series = series;
        this.cards = GenerateCardReward(series, AbstractDungeon.srcUncommonCardPool, AbstractDungeon.srcRareCardPool);
    }

    @Override
    public boolean claimReward()
    {
        if (AbstractDungeon.player.hasRelic(QuestionCard.ID))
        {
            AbstractDungeon.player.getRelic(QuestionCard.ID).flash();
        }

        if (AbstractDungeon.player.hasRelic(BustedCrown.ID))
        {
            AbstractDungeon.player.getRelic(BustedCrown.ID).flash();
        }

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
        {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, TEXT[4]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }

        this.isDone = false;

        return false;
    }

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            return new ConcertsFinalHourReward(CardSeries.GetByID(rewardSave.amount));
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            ConcertsFinalHourReward reward = PCLJUtils.SafeCast(customReward, ConcertsFinalHourReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), null, reward.series.ID, 0);
            }

            return null;
        }
    }
}
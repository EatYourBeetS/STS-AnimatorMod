package pinacolada.rewards.pcl;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardSave;
import pinacolada.resources.GR;
import pinacolada.rewards.PCLReward;
import pinacolada.utilities.PCLJUtils;

public class SpecialGoldReward extends PCLReward
{
    public static final String ID = CreateFullID(SpecialGoldReward.class);

    private static final String GOLD_STRING = CardCrawlGame.languagePack.getUIString("RewardItem").TEXT[1];

    private final String text;

    public SpecialGoldReward(String text, int amount)
    {
        super(ImageMaster.UI_GOLD,amount + GOLD_STRING + " (" + text + ")", GR.Enums.Rewards.SPECIAL_GOLD);

        this.text = text;
        this.goldAmt = amount;
    }

    @Override
    public boolean claimReward()
    {
        CardCrawlGame.sound.play("GOLD_GAIN");
        AbstractDungeon.player.gainGold(this.goldAmt);

        this.isDone = true;

        return true;
    }

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            return new SpecialGoldReward(rewardSave.id, rewardSave.amount);
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            SpecialGoldReward reward = PCLJUtils.SafeCast(customReward, SpecialGoldReward.class);
            if (reward != null)
            {
                return new RewardSave(customReward.type.toString(), reward.text, reward.goldAmt, 0);
            }

            return null;
        }
    }
}
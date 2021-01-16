package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.effects.card.ChooseAndUpgradeEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class UpgradeCommonReward extends AnimatorReward
{
    public static final String ID = CreateFullID(UpgradeCommonReward.class);

    public UpgradeCommonReward()
    {
        super(ID, GR.Animator.Strings.Rewards.UpgradeCard, GR.Enums.Rewards.SYNERGY_CARDS);
    }

    @Override
    public void update()
    {
        super.update();

        if (this.hb.hovered)
        {
            TipHelper.renderGenericTip(360f * Settings.scale, (float) InputHelper.mY, GR.Animator.Strings.Rewards.UpgradeCard, GR.Animator.Strings.Rewards.UpgradeCardDescription);
        }
    }

    @Override
    public boolean claimReward()
    {
        GameEffects.Queue.Add(new ChooseAndUpgradeEffect(c -> {
            return c.rarity.equals(AbstractCard.CardRarity.BASIC) || c.rarity.equals(AbstractCard.CardRarity.COMMON);
        }));

        this.isDone = true;

        return false;
    }

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            return new UpgradeCommonReward();
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            UpgradeCommonReward reward = JUtils.SafeCast(customReward, UpgradeCommonReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), null, 0, 0);
            }

            return null;
        }
    }
}
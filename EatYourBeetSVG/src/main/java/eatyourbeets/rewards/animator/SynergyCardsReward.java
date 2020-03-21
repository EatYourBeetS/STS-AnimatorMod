package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class SynergyCardsReward extends AnimatorReward
{
    public static final String ID = CreateFullID(SynergyCardsReward.class);

    public final Synergy synergy;
    private boolean skip = false;

    private static String GenerateRewardTitle(Synergy synergy)
    {
        if (synergy.ID == Synergies.ANY.ID)
        {
            return "#yColorless";
        }
        else
        {
            return "#y" + synergy.Name.replace(" ", " #y");
        }
    }

    public SynergyCardsReward(Synergy synergy)
    {
        super(ID, GenerateRewardTitle(synergy), GR.Enums.Rewards.SYNERGY_CARDS);

        this.synergy = synergy;
        this.cards = GenerateCardReward(synergy);
    }

    @Override
    public void update()
    {
        super.update();

        if (this.hb.hovered)
        {
            TipHelper.renderGenericTip(360f * Settings.scale, (float) InputHelper.mY, synergy.Name, GR.Animator.Strings.Rewards.Description);
        }
    }

    @Override
    public boolean claimReward()
    {
        if (skip)
        {
            return true;
        }

        ArrayList<RewardItem> rewards = AbstractDungeon.combatRewardScreen.rewards;
        int i = 0;
        while (i < rewards.size())
        {
            SynergyCardsReward other = JavaUtilities.SafeCast(rewards.get(i), SynergyCardsReward.class);
            if (other != null && other != this)
            {
                other.isDone = true;
                other.skip = true;
            }
            i++;
        }

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
            return new SynergyCardsReward(Synergies.GetByID(rewardSave.amount));
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            SynergyCardsReward reward = JavaUtilities.SafeCast(customReward, SynergyCardsReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), null, reward.synergy.ID, 0);
            }

            return null;
        }
    }
}
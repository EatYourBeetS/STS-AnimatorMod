package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class SynergyCardsReward extends AnimatorReward
{
    public static final String ID = CreateFullID(SynergyCardsReward.class);

    public final CardSeries series;
    private boolean skip = false;
    private AnimatorRuntimeLoadout loadout;

    private static String GenerateRewardTitle(CardSeries series)
    {
        if (series.ID == CardSeries.ANY.ID)
        {
            return "#yColorless";
        }
        else
        {
            return "#y" + series.LocalizedName.replace(" ", " #y");
        }
    }

    public SynergyCardsReward(CardSeries series)
    {
        super(ID, GenerateRewardTitle(series), GR.Enums.Rewards.SYNERGY_CARDS);

        this.series = series;
        this.cards = GenerateCardReward(series);
        this.loadout = GR.Animator.Dungeon.GetLoadout(series);
    }

    @Override
    public void update()
    {
        super.update();

        if (this.hb.hovered)
        {
            TipHelper.renderGenericTip(360f * Settings.scale, (float) InputHelper.mY, series.LocalizedName, GR.Animator.Strings.Rewards.Description);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        loadout.card.affinities.Render(sb, hb.x + hb.width * 0.785f, hb.cY - 18, 38);
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
            SynergyCardsReward other = JUtils.SafeCast(rewards.get(i), SynergyCardsReward.class);
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
            return new SynergyCardsReward(CardSeries.GetByID(rewardSave.amount));
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            SynergyCardsReward reward = JUtils.SafeCast(customReward, SynergyCardsReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), null, reward.series.ID, 0);
            }

            return null;
        }
    }
}
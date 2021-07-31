package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardAffinityStatistics;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class SynergyCardsReward extends AnimatorReward
{
    public static final String ID = CreateFullID(SynergyCardsReward.class);

    public final CardSeries series;
    private boolean skip = false;
    private EYBCardAffinityStatistics statistics;
    private EYBCardTooltip tooltip;

    private static String GenerateRewardTitle(CardSeries series)
    {
        if (series.ID == CardSeries.ANY.ID)
        {
            return "#yColorless";
        }
        else
        {
            return JUtils.ModifyString(series.LocalizedName, w -> "#y" + w);
        }
    }

    public SynergyCardsReward(CardSeries series)
    {
        super(ID, GenerateRewardTitle(series), GR.Enums.Rewards.SYNERGY_CARDS);

        this.series = series;
        this.cards = GenerateCardReward(series);

        if (series == CardSeries.ANY)
        {
            statistics = new EYBCardAffinityStatistics(AbstractDungeon.srcColorlessCardPool.group);
            return;
        }

        final AnimatorRuntimeLoadout loadout = GR.Animator.Dungeon.GetLoadout(series);
        if (loadout != null)
        {
            statistics = loadout.AffinityStatistics;
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (this.hb.hovered)
        {
            if (tooltip == null)
            {
                tooltip = new EYBCardTooltip(series.LocalizedName, GR.Animator.Strings.Rewards.Description);

                if (statistics != null)
                {
                    tooltip.description += " NL NL Possible Affinities:";
                    StringBuilder builder = new StringBuilder();
                    for (EYBCardAffinityStatistics.Group g : statistics)
                    {
                        builder.append(JUtils.Format(" NL [A-{0}] : {1} ( #b{2} )", g.Type.Symbol, g.GetPercentageString(0), g.GetTotal(0)));
                    }
                    tooltip.description += builder.toString();
                }
            }

            EYBCardTooltip.QueueTooltip(tooltip, 360 * Settings.scale, InputHelper.mY + 120 * Settings.scale);
            //TipHelper.renderGenericTip(360f * Settings.scale, (float) InputHelper.mY, series.LocalizedName, GR.Animator.Strings.Rewards.Description);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        if (statistics == null)
        {
            return;
        }

        final int MAX = 2;
        int rendered = 0;
        float size, cX, cY;
        for (EYBCardAffinityStatistics.Group g : statistics)
        {
            if (g.Type == AffinityType.Star)
            {
                continue;
            }
            if (rendered >= MAX)
            {
                break;
            }

            size = 42f;
            cX = hb.x + hb.width - ((MAX - rendered) * size * Settings.scale * 1.05f);
            cY = hb.cY + (size * Settings.scale * 0.175f);

            RenderAffinities(g, sb, cX, cY, size, 2);
            rendered += 1;
        }
    }

    private void AdvancedRender(SpriteBatch sb)
    {
        if (statistics == null)
        {
            return;
        }

        int max = AffinityType.BasicTypes().length;
        int borderLevel, i = 0, rendered = 0;
        float size, cX, cY;
        while (rendered < max)
        {
            EYBCardAffinityStatistics.Group group = statistics.GetGroup(i++);
            if (group == null)
            {
                return;
            }
            else if (group.Type == AffinityType.Star)
            {
                continue;
            }

            if (rendered < 2)
            {
                size = 42f;
                cX = hb.x + hb.width - ((2 - rendered) * size);
                cY = hb.y + (size * 0.615f);
                borderLevel = 2;
            }
            else
            {
                size = 28f;
                cX = hb.x + hb.width - ((max - rendered) * size * 1.14f);
                cY = hb.y + hb.height - (size * 0.6f);
                borderLevel = 0;
            }

            RenderAffinities(group, sb, cX, cY, size, borderLevel);
            rendered += 1;
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

    public static void RenderAffinities(EYBCardAffinityStatistics.Group group, SpriteBatch sb, float cX, float cY, float size, int level)
    {
        final AffinityType type = group.Type;
        final BitmapFont font = EYBFontHelper.CardTitleFont_Large;
        font.getData().setScale(size* 0.00925f);

        RenderHelpers.DrawCentered(sb, Color.WHITE, type.GetIcon(), cX, cY, size, size, 1, 0);
        if (level > 0)
        {
            Texture texture = type.GetBorder(level);
            if (texture != null)
            {
                RenderHelpers.DrawCentered(sb, Color.WHITE, texture, cX, cY, size, size, 1, 0);
            }

            texture = type.GetForeground(level);
            if (texture != null)
            {
                RenderHelpers.DrawCentered(sb, Color.WHITE, texture, cX, cY, size, size, 1, 0);
            }
        }
        RenderHelpers.WriteCentered(sb, font, group.GetPercentageString(0), cX + (size * 0.1f * Settings.scale), cY - (size * 0.65f * Settings.scale), Color.WHITE);
        RenderHelpers.ResetFont(font);
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
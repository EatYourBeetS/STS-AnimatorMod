package pinacolada.rewards.pcl;

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
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardAffinityStatistics;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.effects.card.ShowCardPileEffect;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.loadouts._FakeLoadout;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.rewards.PCLReward;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLInputManager;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class MissingPieceReward extends PCLReward
{
    public static final String ID = CreateFullID(MissingPieceReward.class);

    public final CardSeries series;
    public final PCLRuntimeLoadout loadout;
    private boolean skip = false;
    private PCLCardAffinityStatistics statistics;
    private PCLCardTooltip tooltip;

    private static String GenerateRewardTitle(CardSeries series)
    {
        if (series.ID == CardSeries.ANY.ID)
        {
            return PCLJUtils.ModifyString(GR.PCL.Strings.Series.RandomSeries, w -> "#y" + w);
        }
        else if (series.ID == CardSeries.COLORLESS.ID)
        {
            return PCLJUtils.ModifyString(GR.PCL.Strings.Series.Colorless, w -> "#y" + w);
        }
        else
        {
            return PCLJUtils.ModifyString(series.LocalizedName, w -> "#y" + w);
        }
    }

    public MissingPieceReward(CardSeries series)
    {
        super(ID, GenerateRewardTitle(series), GR.Enums.Rewards.SERIES_CARDS);

        this.series = series;
        this.cards = GenerateCardReward(series);

        if (series == CardSeries.COLORLESS)
        {
            statistics = new PCLCardAffinityStatistics(AbstractDungeon.srcColorlessCardPool.group, false);
            loadout = new PCLRuntimeLoadout(new _FakeLoadout());
            return;
        }

        loadout = GR.PCL.Dungeon.GetLoadout(series);
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
                tooltip = new PCLCardTooltip(series.LocalizedName, GR.PCL.Strings.Rewards.Description);

                if (statistics != null)
                {
                    tooltip.description += " NL NL " + GR.PCL.Strings.Rewards.PossibleAffinities + ":";
                    if (loadout != null)
                    {
                        tooltip.description += " NL ( " + GR.PCL.Strings.Rewards.RightClickPreview + " )";
                    }

                    final StringBuilder builder = new StringBuilder();
                    for (PCLCardAffinityStatistics.Group g : statistics)
                    {
                        builder.append(PCLJUtils.Format(" NL [A-{0}] : {1} ( #b{2} )", g.Affinity.Name, g.GetPercentageString(0), g.GetTotal(0)));
                    }

                    tooltip.description += builder.toString();
                }
            }

            if (loadout != null && PCLInputManager.RightClick.IsJustPressed())
            {
                PCLGameEffects.TopLevelQueue.Add(new ShowCardPileEffect(loadout.GetCardPool()));
            }

            PCLCardTooltip.QueueTooltip(tooltip, 360 * Settings.scale, InputHelper.mY + 120 * Settings.scale);
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
        for (PCLCardAffinityStatistics.Group g : statistics)
        {
            if (g.Affinity == PCLAffinity.Star)
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

        int max = PCLAffinity.Extended().length;
        int borderLevel, i = 0, rendered = 0;
        float size, cX, cY;
        while (rendered < max)
        {
            PCLCardAffinityStatistics.Group group = statistics.GetGroup(i++);
            if (group == null)
            {
                return;
            }
            else if (group.Affinity == PCLAffinity.Star)
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
            MissingPieceReward other = PCLJUtils.SafeCast(rewards.get(i), MissingPieceReward.class);
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

    public static void RenderAffinities(PCLCardAffinityStatistics.Group group, SpriteBatch sb, float cX, float cY, float size, int level)
    {
        final PCLAffinity affinity = group.Affinity;
        final BitmapFont font = EYBFontHelper.CardTitleFont_Large;
        font.getData().setScale(size * 0.00925f);

        pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, Color.WHITE, affinity.GetIcon(), cX, cY, size, size, 1, 0);
        if (level > 0)
        {
            Texture texture = affinity.GetBorder(level);
            if (texture != null)
            {
                pinacolada.utilities.PCLRenderHelpers.DrawCentered(sb, Color.WHITE, texture, cX, cY, size, size, 1, 0);
            }
        }
        pinacolada.utilities.PCLRenderHelpers.WriteCentered(sb, font, group.GetPercentageString(0), cX + (size * 0.1f * Settings.scale), cY - (size * 0.65f * Settings.scale), Color.WHITE);
        pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
    }

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            return new MissingPieceReward(CardSeries.GetByID(rewardSave.amount));
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            MissingPieceReward reward = PCLJUtils.SafeCast(customReward, MissingPieceReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), null, reward.series.ID, 0);
            }

            return null;
        }
    }
}
package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.rewards.animator.SynergyCardsReward;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public abstract class AbstractMissingPiece extends AnimatorRelic implements OnReceiveRewardsListener
{
    protected transient AbstractRoom lastRoom = null;
    protected boolean showAffinities = false;

    protected abstract int GetRewardInterval();

    public AbstractMissingPiece(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, tier, sfx);
    }

    public static void RefreshDescription()
    {
        AbstractMissingPiece missingPiece = GameUtilities.GetRelic(AbstractMissingPiece.class);
        if (missingPiece != null)
        {
            if (missingPiece.tips.size() > 0)
            {
                missingPiece.tips.get(0).description = missingPiece.GetFullDescription();
                missingPiece.flash();
            }
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel)
    {
        if (this.counter > -1)
        {
            int actualCounter = GetActualCounter();
            if (inTopPanel)
            {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(actualCounter),
                _offsetX.Get(null) + this.currentX + 30f * Settings.scale, this.currentY - 7f * Settings.scale, Color.WHITE);
            }
            else
            {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(actualCounter),
                this.currentX + 30f * Settings.scale, this.currentY - 7f * Settings.scale, Color.WHITE);
            }
        }

        if (showAffinities)
        {
            GR.UI.CardAffinities.TryRender(sb);
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (showAffinities)
        {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
            {
                GR.UI.CardAffinities.TryUpdate();
            }
            else
            {
                showAffinities = false;
            }
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(GetRewardInterval());
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        SetCounter(0);
        RefreshDescription();
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        if (RewardsAllowed())
        {
            AddCounter(1);
        }
    }

    public void OnReceiveRewards(ArrayList<RewardItem> rewards)
    {
        if (counter > 0 && RewardsAllowed() && GetActualCounter() == 0)
        {
            this.flash();
            lastRoom = GameUtilities.GetCurrentRoom();

            int startingIndex = -1;
            for (int i = 0; i < rewards.size(); i++)
            {
                RewardItem reward = rewards.get(i);
                if (reward.type == RewardItem.RewardType.CARD)
                {
                    startingIndex = i;
                    rewards.remove(startingIndex);
                    break;
                }
            }

            if (startingIndex >= 0)
            {
                AddSynergyRewards(rewards, startingIndex);
            }
        }
    }

    public String GetFullDescription()
    {
        String base = getUpdatedDescription();
        if (GR.Animator.Dungeon.Loadouts.isEmpty())
        {
            return base;
        }

        StringJoiner joiner = new StringJoiner(" NL ");
        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Loadouts)
        {
            if (series.promoted)
            {
                String line = "- #y" + StringUtils.replace(series.Loadout.Name, " ", " #y");
                if (series.bonus > 0)
                {
                    line += " #y( " + series.bonus + "/6 #y)";
                }

                joiner.add(line);
            }
            else
            {
                joiner.add("- " + series.Loadout.Name);
            }
        }

        return base + " NL  NL " + DESCRIPTIONS[1] + " NL " + joiner.toString();
    }

    private void AddSynergyRewards(ArrayList<RewardItem> rewards, int startingIndex)
    {
        WeightedList<CardSeries> synergies = CreateWeightedList();

        for (int i = 0; i < 3; i++)
        {
            CardSeries series = synergies.Retrieve(AbstractDungeon.cardRng);
            if (series != null)
            {
                rewards.add(startingIndex + i, new SynergyCardsReward(series));
            }
        }

        GR.UI.CardAffinities.Open(player.masterDeck.group, null);
        showAffinities = true;
    }

    private WeightedList<CardSeries> CreateWeightedList()
    {
        WeightedList<CardSeries> list = new WeightedList<>();
        Map<CardSeries, List<AbstractCard>> synergyListMap = CardSeries.GetCardsBySynergy(player.masterDeck.group);

        if (GR.Animator.Dungeon.Loadouts.isEmpty())
        {
            GR.Animator.Dungeon.AddAllLoadouts();
        }

        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Loadouts)
        {
            if (series.Cards.size() >= 10)
            {
                CardSeries s = series.Loadout.Series;

                int weight = series.promoted ? 5 : 2;
                if (synergyListMap.containsKey(s))
                {
                    int size = synergyListMap.get(s).size();
                    if (size >= 2)
                    {
                        weight += 12 + (size * 3);
                        if (weight > 26)
                        {
                            weight = 26;
                        }
                    }
                }

                JUtils.LogInfo(this, s.LocalizedName + " : " + weight);
                list.Add(s, weight);
            }
        }

        if (relicId.equals(ColorlessFragment.ID))
        {
            list.Add(CardSeries.ANY, ColorlessFragment.COLORLESS_WEIGHT);
        }

        return list;
    }

    private boolean RewardsAllowed()
    {
        final AbstractRoom room = GameUtilities.GetCurrentRoom();
        return room != null && lastRoom != room && room.rewardAllowed
        && !(room instanceof MonsterRoomBoss)
        && (room instanceof MonsterRoom || room.eliteTrigger); // || (room instanceof EventRoom && room.combatEvent));
    }

    private int GetActualCounter()
    {
        return this.counter % GetRewardInterval();
    }
}
package eatyourbeets.relics.animatorClassic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.relics.AnimatorClassicRelic;
import eatyourbeets.rewards.animator.MissingPieceReward;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public abstract class AbstractMissingPiece extends AnimatorClassicRelic implements OnReceiveRewardsListener
{
    protected transient AbstractRoom lastRoom = null;

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
                missingPiece.tips.get(0).description = missingPiece.description;
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
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, GetRewardInterval());
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

//    public String GetFullDescription()
//    {
//        String base = getUpdatedDescription();
//        if (GR.Animator.Dungeon.Series.isEmpty())
//        {
//            return base;
//        }
//
//        StringJoiner joiner = new StringJoiner(" NL ");
//        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
//        {
//            if (series.promoted)
//            {
//                String line = "- #y" + StringUtils.replace(series.Loadout.Name, " ", " #y");
//                if (series.bonus > 0)
//                {
//                    line += " #y( " + series.bonus + "/6 #y)";
//                }
//
//                joiner.add(line);
//            }
//            else
//            {
//                joiner.add("- " + series.Loadout.Name);
//            }
//        }
//
//        return base + " NL  NL " + DESCRIPTIONS[1] + " NL " + joiner.toString();
//    }

    private void AddSynergyRewards(ArrayList<RewardItem> rewards, int startingIndex)
    {
        WeightedList<CardSeries> synergies = CreateWeightedList();

        for (int i = 0; i < 3; i++)
        {
            CardSeries synergy = synergies.Retrieve(AbstractDungeon.cardRng);
            if (synergy != null)
            {
                rewards.add(startingIndex + i, new MissingPieceReward(synergy));
            }
        }
    }

    private WeightedList<CardSeries> CreateWeightedList()
    {
        WeightedList<CardSeries> list = new WeightedList<>();
        return list;
//        Map<CardSeries, List<AbstractCard>> synergyListMap = CardSeries.GetAllSeries(player.masterDeck.group);
//
////        if (GR.Animator.Dungeon.Series.isEmpty())
////        {
////            GR.Animator.Dungeon.AddAllSeries();
////        }
//
//        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
//        {
//            if (series.Cards.size() >= 10)
//            {
//                CardSeries s = series.Loadout.Synergy;
//
//                int weight = series.promoted ? 5 : 2;
//                if (synergyListMap.containsKey(s))
//                {
//                    int size = synergyListMap.get(s).size();
//                    if (size >= 2)
//                    {
//                        weight += 12 + (size * 3);
//                        if (weight > 26)
//                        {
//                            weight = 26;
//                        }
//                    }
//                }
//
//                JUtils.LogInfo(this, s.LocalizedName + " : " + weight);
//                list.Add(s, weight);
//            }
//        }
//
//        if (relicId.equals(eatyourbeets.relics.deprecated.ColorlessFragment.ID))
//        {
//            list.Add(Synergies.ANY, ColorlessFragment.COLORLESS_WEIGHT);
//        }
//
//        return list;
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
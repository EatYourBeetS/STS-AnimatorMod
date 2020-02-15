package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.interfaces.subscribers.OnReceiveRewardsSubscriber;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.rewards.animator.SynergyCardsReward;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.WeightedList;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract class AbstractMissingPiece extends AnimatorRelic implements OnReceiveRewardsSubscriber
{
    private boolean skipReward;

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
                missingPiece.tips.get(0).body = missingPiece.getFullDescription();
                missingPiece.flash();
            }
        }
    }

    public String getFullDescription()
    {
        String base = getUpdatedDescription();
        if (GR.Animator.Dungeon.Series.isEmpty())
        {
            return base;
        }

        StringJoiner joiner = new StringJoiner(" NL ");
        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
        {
            if (series.promoted)
            {
                String line = "- #y" + StringUtils.replace(series.Loadout.Name," ", " #y");
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

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(GetRewardInterval());
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        this.counter = 1;
        RefreshDescription();
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        if (AbstractDungeon.actNum == 1 && AbstractDungeon.getCurrMapNode().y == 0)
        {
            skipReward = true;
            return;
        }

        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room == null || room instanceof MonsterRoomBoss || room instanceof TreasureRoomBoss)
        {
            skipReward = true;
            return;
        }

        if (room.rewardAllowed)
        {
            this.counter += 1;
            this.skipReward = false;
        }
    }

    public void OnReceiveRewards(ArrayList<RewardItem> rewards)
    {
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room == null || room instanceof MonsterRoomBoss || room instanceof TreasureRoomBoss)
        {
            return;
        }

        if (counter == 0)
        {
            if (skipReward)
            {
                return;
            }

            skipReward = true;
        }
        else if (counter < GetRewardInterval())
        {
            return;
        }

        counter = 0;
        this.flash();

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
            addSynergyRewards(rewards, startingIndex);
        }

        if (counter == 0)
        {
            this.skipReward = true;
        }
    }

    private void addSynergyRewards(ArrayList<RewardItem> rewards, int startingIndex)
    {
        WeightedList<Synergy> synergies = CreateWeightedList();

        for (int i = 0; i < 3; i++)
        {
            Synergy synergy = synergies.Retrieve(AbstractDungeon.cardRng);
            if (synergy != null)
            {
                rewards.add(startingIndex + i, new SynergyCardsReward(synergy));
            }
        }
    }

    private WeightedList<Synergy> CreateWeightedList()
    {
        WeightedList<Synergy> list = new WeightedList<>();
        Map<Synergy, List<AbstractCard>> synergyListMap = GetCardsBySynergy(AbstractDungeon.player.masterDeck.group);

        if (GR.Animator.Dungeon.Series.isEmpty())
        {
            GR.Animator.Dungeon.AddAllSeries();
        }

        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Series)
        {
            if (series.Cards.size() >= 10)
            {
                Synergy s = series.Loadout.Synergy;

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

                logger.info(s.Name + " : " + weight);
                list.Add(s, weight);
            }
        }

        if (relicId.equals(ColorlessFragment.ID))
        {
            list.Add(Synergies.ANY, ColorlessFragment.COLORLESS_WEIGHT);
        }

        return list;
    }

    private Map<Synergy, List<AbstractCard>> GetCardsBySynergy(ArrayList<AbstractCard> cards)
    {
        Map<Synergy, List<AbstractCard>> map = new HashMap<>();
        for(AbstractCard card : cards)
        {
            Synergy key = Synergies.ANY;
            AnimatorCard c = JavaUtilities.SafeCast(card, AnimatorCard.class);
            if (c != null && c.synergy != null)
            {
                key = c.synergy;
            }

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(card);
        }

        return map;
    }
}
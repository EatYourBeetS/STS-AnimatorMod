package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.utilities.WeightedList;
import eatyourbeets.rewards.SynergyCardsReward;
import eatyourbeets.interfaces.OnReceiveRewardsSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractMissingPiece extends AnimatorRelic implements OnReceiveRewardsSubscriber
{
    private static final ArrayList<Synergy> possibleRewards = new ArrayList<>();

    private boolean skipReward;

    public AbstractMissingPiece(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, tier, sfx);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], GetRewardInterval());
    }

    protected abstract int GetRewardInterval();

    @Override
    public void onEquip()
    {
        super.onEquip();
        this.counter = 1;
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
        Map<Synergy, List<AbstractCard>> synergyListMap = AbstractDungeon.player.masterDeck.group.stream().collect(Collectors.groupingBy(this::Group));

        PurgingStone_Cards purgingStone = PurgingStone_Cards.GetInstance();

        for (Synergy s : possibleRewards)
        {
//            if (purgingStone == null || !purgingStone.IsBanned(s))
//            {
            int weight = 2;
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

            logger.info(s.NAME + " : " + weight);
            list.Add(s, weight);
//            }
        }

        if (relicId.equals(ColorlessFragment.ID))
        {
            list.Add(Synergies.ANY, ColorlessFragment.COLORLESS_WEIGHT);
        }

        return list;
    }

    private Synergy Group(AbstractCard card)
    {
        AnimatorCard c = JavaUtilities.SafeCast(card, AnimatorCard.class);

        Synergy synergy = null;
        if (c != null)
        {
            synergy = c.synergy;
        }

        return synergy != null ? synergy : Synergies.ANY;
    }

    static
    {
        possibleRewards.add(Synergies.Gate);
        possibleRewards.add(Synergies.Overlord);
        possibleRewards.add(Synergies.NoGameNoLife);
        possibleRewards.add(Synergies.Chaika);
        possibleRewards.add(Synergies.Katanagatari);
        possibleRewards.add(Synergies.Fate);
        possibleRewards.add(Synergies.Elsword);
        possibleRewards.add(Synergies.Konosuba);
        possibleRewards.add(Synergies.OwariNoSeraph);
        possibleRewards.add(Synergies.GoblinSlayer);
        possibleRewards.add(Synergies.FullmetalAlchemist);
        possibleRewards.add(Synergies.TenSura);
        possibleRewards.add(Synergies.OnePunchMan);
    }
}
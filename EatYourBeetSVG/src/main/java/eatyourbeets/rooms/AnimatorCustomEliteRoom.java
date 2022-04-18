package eatyourbeets.rooms;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.listeners.OnRoomTransitionListener;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class AnimatorCustomEliteRoom extends MonsterRoomElite implements OnRoomTransitionListener
{
    public interface GetElite
    {
        MonsterGroup get();
    }

    public final boolean removeNormalRewards;
    public final String monsterGroupID;

    private final GetElite getElite;
    private final ActionT1<ArrayList<RewardItem>> modifyRewards;
    private boolean rewardsModified;

    public AnimatorCustomEliteRoom(GetElite getElite, String monsterGroupID, ActionT1<ArrayList<RewardItem>> modifyRewards, boolean removeNormalRewards)
    {
        this.modifyRewards = modifyRewards;
        this.monsterGroupID = monsterGroupID;
        this.removeNormalRewards = removeNormalRewards;
        this.getElite = getElite;
    }

    @Override
    public void update()
    {
        if (modifyRewards != null && !rewardsModified && phase == RoomPhase.COMBAT && (isBattleOver && AbstractDungeon.actionManager.actions.isEmpty()))
        {
            modifyRewards.Invoke(rewards);
            rewardsModified = true;
        }

        super.update();
    }

    @Override
    public void onPlayerEntry()
    {
        if (monsters == null)
        {
            AbstractDungeon.lastCombatMetricKey = monsterGroupID;
            monsters = getElite.get();
            monsters.init();
        }

        super.onPlayerEntry();
    }

    @Override
    public void setMonster(MonsterGroup m)
    {
        JUtils.LogWarning(this, "This room can only spawn " + JUtils.JoinStrings(",", monsters.getMonsterNames()));
    }

    @Override
    public void OnRoomTransition(AbstractRoom room)
    {
        if (room == this)
        {
            AbstractDungeon.eliteMonsterList.add(0, monsterGroupID);
        }
    }

    //REWARDS


    @Override
    public void removeOneRelicFromRewards()
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.removeOneRelicFromRewards();
    }

    @Override
    public void addStolenGoldToRewards(int gold)
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addStolenGoldToRewards(gold);
    }

    @Override
    public void addRelicToRewards(AbstractRelic relic)
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addRelicToRewards(relic);
    }

    @Override
    public void addNoncampRelicToRewards(AbstractRelic.RelicTier tier)
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addNoncampRelicToRewards(tier);
    }

    @Override
    public void addRelicToRewards(AbstractRelic.RelicTier tier)
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addRelicToRewards(tier);
    }

    @Override
    public void addCardToRewards()
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addCardToRewards();
    }

    @Override
    public void dropReward()
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.dropReward();
    }

    @Override
    public void addCardReward(RewardItem rewardItem)
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addCardReward(rewardItem);
    }

    @Override
    public void addPotionToRewards(AbstractPotion potion)
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addPotionToRewards(potion);
    }

    @Override
    public void addGoldToRewards(int gold)
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addGoldToRewards(gold);
    }

    @Override
    public void addPotionToRewards()
    {
        if (removeNormalRewards)
        {
            return;
        }

        super.addPotionToRewards();
    }
}

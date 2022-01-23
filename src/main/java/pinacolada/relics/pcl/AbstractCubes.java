package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public abstract class AbstractCubes extends PCLRelic
{
    private static final FieldInfo<Boolean> _isBoss = PCLJUtils.GetField("isBoss", RewardItem.class);

    public static final String ID = CreateFullID(AbstractCubes.class);
    public final int normalUses;
    public final int eliteUses;
    public final int maxUses;

    public AbstractCubes(String id, RelicTier tier, LandingSound sfx, int normalUses, int eliteUses, int maxUses)
    {
        super(id, tier, sfx);
        this.normalUses = normalUses;
        this.eliteUses = eliteUses;
        this.maxUses = maxUses;
        this.updateDescription(null);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, normalUses, eliteUses, maxUses);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetCounter(1);
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        super.onEnterRoom(room);

        if (room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
        {
            SetCounter(Math.min(maxUses, counter + eliteUses + normalUses));
            flash();
        }
        else if (room instanceof MonsterRoom && normalUses > 0) {
            SetCounter(Math.min(maxUses, counter + normalUses));
            flash();
        }
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        return CanReroll() && !PCLGameUtilities.InBattle() && rewardItem != null && (rewardItem.type == RewardItem.RewardType.CARD || rewardItem.type == GR.Enums.Rewards.SERIES_CARDS) && !_isBoss.Get(rewardItem);
    }

    public boolean CanReroll()
    {
        return counter > 0;
    }

    public AbstractCard Reroll(AbstractCard card, RewardItem rewardItem)
    {
        SetCounter(counter - 1);
        return GetReward(card, rewardItem);
    }

    public AbstractCard GetReward(AbstractCard card, RewardItem rewardItem) {
        return GR.PCL.Dungeon.GetRandomRewardCard(rewardItem.cards, false,true);
    }
}
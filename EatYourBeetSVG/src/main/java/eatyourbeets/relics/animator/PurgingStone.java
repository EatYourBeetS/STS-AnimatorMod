package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class PurgingStone extends AnimatorRelic
{
    private static final FieldInfo<Boolean> _isBoss = JUtils.GetField("isBoss", RewardItem.class);

    public static final String ID = CreateFullID(PurgingStone.class);
    public static final int MAX_STORED_USES = 3;
    public static final int USES_PER_ELITE = 2;

    public PurgingStone()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, USES_PER_ELITE, MAX_STORED_USES);
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
            SetCounter(Math.min(MAX_STORED_USES, counter + USES_PER_ELITE));
            flash();
        }
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        return CanReroll() && !GameUtilities.InBattle() && rewardItem != null && rewardItem.type == RewardItem.RewardType.CARD && !_isBoss.Get(rewardItem);
    }

    public boolean CanReroll()
    {
        return counter > 0;
    }

    public AbstractCard Reroll(RewardItem rewardItem)
    {
        SetCounter(counter - 1);
        return GR.Common.Dungeon.GetRandomRewardCard(rewardItem.cards, false,true);
    }
}
package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class RollingCubes extends PCLRelic
{
    private static final FieldInfo<Boolean> _isBoss = PCLJUtils.GetField("isBoss", RewardItem.class);
    private static final CardGroup tempGroup1 = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
    private static final CardGroup tempGroup2 = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

    public static final String ID = CreateFullID(RollingCubes.class);
    public static final int MAX_STORED_USES = 5;
    public static final int USES_PER_ELITE = 3;
    public static final int USES_PER_NORMAL = 1;

    public RollingCubes()
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
        return CanReroll() && !PCLGameUtilities.InBattle() && rewardItem != null && (rewardItem.type == RewardItem.RewardType.CARD || rewardItem.type == GR.Enums.Rewards.SERIES_CARDS) && !_isBoss.Get(rewardItem);
    }

    public boolean CanReroll()
    {
        return counter > 0;
    }

    public AbstractCard Reroll(AbstractCard card, RewardItem rewardItem)
    {
        SetCounter(counter - 1);

        final PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
        if (c != null && c.series != null)
        {
            final PCLRuntimeLoadout loadout = GR.PCL.Dungeon.GetLoadout(c.series);
            if (loadout != null)
            {
                final CardGroup g1 = AbstractDungeon.srcCommonCardPool;
                final CardGroup g2 = AbstractDungeon.srcUncommonCardPool;

                tempGroup1.clear();
                tempGroup2.clear();

                for (AbstractCard sc : loadout.GetCardPoolInPlay().values())
                {
                    if (g1.contains(sc))
                    {
                        tempGroup1.addToTop(sc);
                    }
                    else if (g2.contains(sc))
                    {
                        tempGroup2.addToTop(sc);
                    }
                }

                AbstractDungeon.srcCommonCardPool = tempGroup1;
                AbstractDungeon.srcUncommonCardPool = tempGroup2;

                final AbstractCard reward = GR.PCL.Dungeon.GetRandomRewardCard(rewardItem.cards, false,true);

                AbstractDungeon.srcCommonCardPool = g1;
                AbstractDungeon.srcUncommonCardPool = g2;

                return reward;
            }
        }

        return GR.PCL.Dungeon.GetRandomRewardCard(rewardItem.cards, false,true);
    }
}
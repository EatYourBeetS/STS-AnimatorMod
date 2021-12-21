package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class PurgingStone extends PCLRelic
{
    private static final FieldInfo<Boolean> _isBoss = PCLJUtils.GetField("isBoss", RewardItem.class);

    public static final String ID = CreateFullID(PurgingStone.class);
    public static final int MAX_BAN_COUNT = 80;
    public static final int MAX_STORED_USES = 3;
    public static final int USES_PER_ELITE = 2;

    public PurgingStone()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, MAX_STORED_USES);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetCounter(0);
        AddUses(0);
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        super.onEnterRoom(room);

        if (room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
        {
            AddUses(USES_PER_ELITE);
            flash();
        }
    }

    public int GetBannedCount()
    {
        return GR.PCL.Dungeon.BannedCards.size();
    }

    private void AddUses(int uses)
    {
        int banned = GetBannedCount();
        if (AddCounter(uses) + banned > MAX_BAN_COUNT)
        {
            SetCounter(MAX_BAN_COUNT - banned);
        }
        if (counter > MAX_STORED_USES)
        {
            SetCounter(MAX_STORED_USES);
        }
    }

    public boolean IsBanned(String cardID)
    {
        return GR.PCL.Dungeon.BannedCards.contains(cardID);
    }

    public boolean IsBanned(AbstractCard card)
    {
        return card != null && IsBanned(card.cardID);
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        if (!PCLGameUtilities.InBattle() && rewardItem != null && (rewardItem.type == RewardItem.RewardType.CARD || rewardItem.type == GR.Enums.Rewards.SERIES_CARDS) && !_isBoss.Get(rewardItem))
        {
            return counter > 0;
        }

        return false;
    }

    public boolean CanBan(AbstractCard card)
    {
        if (counter > 0)
        {
            if (card.color == AbstractCard.CardColor.COLORLESS)
            {
                return false;
            }

            if (!GR.PCL.Dungeon.BannedCards.contains(card.cardID))
            {
                CardGroup pool = PCLGameUtilities.GetCardPoolSource(card.rarity);
                if (pool == null)
                {
                    return false;
                }
                else if (card.type == AbstractCard.CardType.POWER)
                {
                    return card.rarity == AbstractCard.CardRarity.COMMON || pool.getPowers().size() >= 2;
                }
                else if (card.type == AbstractCard.CardType.SKILL)
                {
                    return pool.getSkills().size() >= 3;
                }
                else if (card.type == AbstractCard.CardType.ATTACK)
                {
                    return pool.getAttacks().size() >= 3;
                }
            }
        }

        return false;
    }

    public void Ban(AbstractCard card)
    {
        GR.PCL.Dungeon.Ban(card.cardID);
        AddCounter(-1);
        flash();
    }
}
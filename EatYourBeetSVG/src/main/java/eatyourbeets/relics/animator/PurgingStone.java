package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class PurgingStone extends AnimatorRelic
{
    public static final String ID = CreateFullID(PurgingStone.class);

    private static final FieldInfo<Boolean> _isBoss = JavaUtilities.GetField("isBoss", RewardItem.class);
    private static final int MAX_BAN_COUNT = 80;
    private static final int MAX_STORED_USES = 3;

    public PurgingStone()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(MAX_STORED_USES);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        counter = 0;

        AddUses(0);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.flash();

        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
        {
            AddUses(2);
        }
        else
        {
            AddUses(1);
        }
    }

    public int GetBannedCount()
    {
        return GR.Animator.Dungeon.BannedCards.size();
    }

    private void AddUses(int uses)
    {
        int banned = GetBannedCount();

        counter += uses;
        if (counter + banned > MAX_BAN_COUNT)
        {
            counter = MAX_BAN_COUNT - banned;
        }

        if (counter > MAX_STORED_USES)
        {
            counter = MAX_STORED_USES;
        }
    }

    public boolean IsBanned(String cardID)
    {
        return GR.Animator.Dungeon.BannedCards.contains(cardID);
    }

    public boolean IsBanned(AbstractCard card)
    {
        return card != null && IsBanned(card.cardID);
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        if (!GameUtilities.InBattle() && rewardItem != null && rewardItem.type == RewardItem.RewardType.CARD && !_isBoss.Get(rewardItem))
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

            if (!GR.Animator.Dungeon.BannedCards.contains(card.cardID))
            {
                CardGroup pool = GameUtilities.GetCardPoolSource(card.rarity, card.color);
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
        GR.Animator.Dungeon.Ban(card.cardID);
        counter -= 1;
        flash();
    }
}
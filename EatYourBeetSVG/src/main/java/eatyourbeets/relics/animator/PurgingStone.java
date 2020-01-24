package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class PurgingStone extends AnimatorRelic implements CustomSavable<String>
{
    public static final String ID = CreateFullID(PurgingStone.class.getSimpleName());

    private static final FieldInfo<Boolean> _isBoss = JavaUtilities.GetField("isBoss", RewardItem.class);
    private static final int MAX_BAN_COUNT = 80;
    private static final int MAX_STORED_USES = 3;
    private final ArrayList<String> bannedCards = new ArrayList<>();

    public PurgingStone()
    {
        super(ID, RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(MAX_STORED_USES);
    }

    public static void UpdateBannedCards()
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (player != null && player.relics != null)
        {
            for (AbstractRelic relic : player.relics)
            {
                if (relic instanceof PurgingStone)
                {
                    ((PurgingStone)relic).UpdateBannedCardsInternal();
                }
            }
        }
    }

    public ArrayList<String> GetBannedCards()
    {
        return new ArrayList<>(bannedCards);
    }

    public int GetBannedCount()
    {
        return bannedCards.size();
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

    private void AddUses(int uses)
    {
        int banned = bannedCards.size();

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
        return bannedCards.contains(cardID);
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

            if (!bannedCards.contains(card.cardID))
            {
                CardGroup pool;
                switch (card.rarity)
                {
                    case COMMON:
                    {
                        pool = AbstractDungeon.srcCommonCardPool;
                        break;
                    }

                    case UNCOMMON:
                    {
                        pool = AbstractDungeon.srcUncommonCardPool;
                        break;
                    }

                    case RARE:
                    {
                        pool = AbstractDungeon.srcRareCardPool;
                        break;
                    }

                    case SPECIAL:
                    case BASIC:
                    case CURSE:
                    default:
                        return false;
                }

                if (card.type == AbstractCard.CardType.POWER)
                {
                    return pool.getPowers().size() >= 2;
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
        RemoveCard(card);
        bannedCards.add(card.cardID);

        counter -= 1;
        this.flash();
    }

    @Override
    public String onSave()
    {
        ArrayList<String> result = new ArrayList<>();
        for (String card : bannedCards)
        {
            result.add(String.valueOf(card));
        }

        return String.join("\u001F", result);
    }

    @Override
    public void onLoad(String value)
    {
        for (String s : value.split("\u001F"))
        {
            if (s != null && !s.isEmpty())
            {
                bannedCards.add(s);
            }
        }

        AddUses(0);
    }

    private void RemoveCard(AbstractCard card)
    {
        if (card == null)
        {
            return;
        }

        int banCount = 0;
        int srcBanCount = 0;

        CardGroup pool;
        CardGroup srcPool;

        switch (card.rarity)
        {
            case COMMON:
                pool    = AbstractDungeon.srcCommonCardPool;
                srcPool = AbstractDungeon.commonCardPool;
                break;

            case UNCOMMON:
                pool    = AbstractDungeon.srcUncommonCardPool;
                srcPool = AbstractDungeon.uncommonCardPool;
                break;

            case RARE:
                pool    = AbstractDungeon.srcRareCardPool;
                srcPool = AbstractDungeon.rareCardPool;
                break;

            case BASIC:
            case SPECIAL:
            case CURSE:
            default:
                pool = null;
                srcPool = null;
                break;
        }

        if (pool != null)
        {
            pool.removeCard(card.cardID);
            banCount++;
        }

        if (srcPool != null)
        {
            srcPool.removeCard(card.cardID);
            srcBanCount++;
        }

        logger.info("Banned " + card.cardID + " " + banCount + ", " + srcBanCount);
    }

    public void UpdateBannedCardsInternal()
    {
        logger.info("Banned " + bannedCards.size() + " Cards:");
        for (String card : bannedCards)
        {
            RemoveCard(CardLibrary.getCard(card));
        }
    }
}
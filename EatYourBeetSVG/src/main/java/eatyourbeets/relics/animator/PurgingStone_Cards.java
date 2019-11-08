package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.cards.CardSeriesComparator;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.Utilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

public class PurgingStone_Cards extends AnimatorRelic implements CustomSavable<String>
{
    public static final String ID = CreateFullID(PurgingStone_Cards.class.getSimpleName());

    private static final int MAX_BAN_COUNT = 80;
    private static final int MAX_STORED_USES = 3;
    private static Field isBoss = null;
    private final ArrayList<String> bannedCards = new ArrayList<>();

    @Override
    public String getUpdatedDescription()
    {
        return Utilities.Format(DESCRIPTIONS[0], MAX_STORED_USES);
    }

    public PurgingStone_Cards()
    {
        super(ID, CreateFullID("PurgingStone"), RelicTier.STARTER, LandingSound.SOLID);

        if (isBoss == null)
        {
            try
            {
                isBoss = RewardItem.class.getDeclaredField("isBoss");
                isBoss.setAccessible(true);
            }
            catch (NoSuchFieldException e)
            {
                e.printStackTrace();
                isBoss = null;
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

    @Override
    public void update()
    {
        super.update();

        if (HitboxRightClick.rightClicked.get(this.hb) && !PlayerStatistics.InBattle() &&
                AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID && bannedCards.size() > 0)
        {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for (String cardID : bannedCards)
            {
                AbstractCard card = CardLibrary.getCard(cardID);
                if (card != null)
                {
                    group.addToBottom(card.makeCopy());
                }
            }

            if (group.size() == 0)
            {
                return;
            }

            group.group.sort(new CardRarityComparator());
            group.group.sort(new CardSeriesComparator());

            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.gridSelectScreen.open(group, 0, false, "");
        }
    }

    private class CardRarityComparator implements Comparator<AbstractCard>
    {
        private CardRarityComparator() {
        }

        public int compare(AbstractCard c1, AbstractCard c2) {
            return c1.rarity.compareTo(c2.rarity);
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

    public static PurgingStone_Cards GetInstance()
    {
        if (AbstractDungeon.player == null || AbstractDungeon.player.relics == null)
        {
            return null;
        }

        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof PurgingStone_Cards)
            {
                return (PurgingStone_Cards)r;
            }
        }

        return null;
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
        if (!PlayerStatistics.InBattle() && rewardItem != null && rewardItem.type == RewardItem.RewardType.CARD)
        {
            try
            {
                if (!(boolean)isBoss.get(rewardItem))
                {
                    return counter > 0;
                }
            }
            catch (IllegalAccessException | ClassCastException e)
            {
                e.printStackTrace();
            }
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

    public void UpdateBannedCards()
    {
        logger.info("Banned " + bannedCards.size() + " Cards:");
        for (String card : bannedCards)
        {
            RemoveCard(CardLibrary.getCard(card));
        }
    }
}
package eatyourbeets.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.Utilities;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;
import java.util.Collections;

public class PurgingStone extends AnimatorRelic implements CustomSavable<String>
{
    public static final String ID = CreateFullID(PurgingStone.class.getSimpleName());

    private static final int CHARGES = 4;

    private final ArrayList<String> bannedCards;

    public PurgingStone()
    {
        super(ID, RelicTier.STARTER, LandingSound.CLINK);

        bannedCards = new ArrayList<>();
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new PurgingStone();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        this.counter = CHARGES + 1;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room.rewardAllowed && room instanceof MonsterRoomBoss)
        {
            if (bannedCards.size() < 40)
            {
                this.counter += CHARGES;
            }
        }
    }

    public static PurgingStone GetInstance()
    {
        if (AbstractDungeon.player == null || AbstractDungeon.player.relics == null)
        {
            return null;
        }

        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof PurgingStone)
            {
                return (PurgingStone)r;
            }
        }

        return null;
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        return (!PlayerStatistics.InBattle() && bannedCards.size() < 40 && rewardItem != null && rewardItem.type == RewardItem.RewardType.CARD);
    }

    public boolean CanBan(AbstractCard card)
    {
        return counter > 0 && !bannedCards.contains(card.cardID);
    }

    public void Ban(AbstractCard card)
    {
        bannedCards.add(card.cardID);
        counter -= 1;
        this.flash();
        //logger.info(bannedCards.size() + ": " + onSave());
    }

    @Override
    public String onSave()
    {
        return String.join("\u001F", bannedCards);
    }

    @Override
    public void onLoad(String value)
    {
        Collections.addAll(bannedCards, value.split("\u001F"));
    }

    public void receiveRewards(ArrayList<RewardItem> rewards)
    {
        if (rewards == null)
        {
            return;
        }

        //logger.info("Received Rewards");
        for (RewardItem r : rewards)
        {
            if (r != null && r.type == RewardItem.RewardType.CARD)
            {
//                StringJoiner sj = new StringJoiner(",");
//                for (AbstractCard c : r.cards)
//                {
//                    sj.add(c.cardID);
//                }
//                logger.info(sj.toString());

                for (int i = r.cards.size() - 1; i >= 0; i--)
                {
                    if (bannedCards.contains(r.cards.get(i).cardID))
                    {
                        int cycles = 30;
                        AbstractCard card;
                        do
                        {
                            card = GetRandomCard(r.cards);
                        }
                        while (card == null && cycles-- > 0);

                        if (card != null)
                        {
                            r.cards.set(i, card.makeCopy());
                        }
                        else
                        {
                            r.cards.remove(i);
                        }
                    }
                }
            }
        }
    }

    private AbstractCard GetRandomCard(ArrayList<AbstractCard> rewards)
    {
        AbstractCard.CardRarity rarity = AbstractDungeon.rollRarity();
        ArrayList<AbstractCard> pool = null;
        switch (rarity)
        {
            case COMMON:
                pool = AbstractDungeon.srcCommonCardPool.group;
                break;

            case UNCOMMON:
                pool = AbstractDungeon.srcUncommonCardPool.group;
                break;

            case RARE:
                pool = AbstractDungeon.srcRareCardPool.group;
                break;

            case BASIC:
            case SPECIAL:
            case CURSE:
                break;
        }

        if (pool == null || pool.size() == 0)
        {
            return null;
        }

        ArrayList<String> rewardsID = new ArrayList<>();
        for (AbstractCard c : rewards)
        {
            rewardsID.add(c.cardID);
        }

        ArrayList<AbstractCard> temp = new ArrayList<>();
        for (AbstractCard c : pool)
        {
            if (!bannedCards.contains(c.cardID) && !rewardsID.contains(c.cardID))
            {
                temp.add(c);
            }
        }

        return Utilities.GetRandomElement(temp, AbstractDungeon.cardRng);
    }
}
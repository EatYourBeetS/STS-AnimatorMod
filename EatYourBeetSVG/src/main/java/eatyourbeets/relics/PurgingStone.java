package eatyourbeets.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;
import java.util.Collections;

public class PurgingStone extends AnimatorRelic implements CustomSavable<String>
{
    public static final String ID = CreateFullID(PurgingStone.class.getSimpleName());

    private final ArrayList<Synergy> bannedSynergies;

    public PurgingStone()
    {
        super(ID, RelicTier.STARTER, LandingSound.CLINK);

        bannedSynergies = new ArrayList<>();
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

        counter = 0;
        AddUses(2);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (room.rewardAllowed && room instanceof MonsterRoomBoss)
        {
            AddUses(1);
        }
    }

    private void AddUses(int uses)
    {
        int max = (Synergies.Count() - 5);
        int banned = bannedSynergies.size();

        counter += uses;
        if (counter + banned > max)
        {
            counter = max - banned;
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

    public boolean IsBanned(Synergy synergy)
    {
        return bannedSynergies.contains(synergy);
    }

    public boolean IsBanned(AbstractCard card)
    {
        AnimatorCard animatorCard = Utilities.SafeCast(card, AnimatorCard.class);
        if (animatorCard != null)
        {
            return IsBanned(animatorCard.GetSynergy());
        }

        return false;
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        if (!PlayerStatistics.InBattle() && rewardItem != null && rewardItem.type == RewardItem.RewardType.CARD)
        {
            return counter > 0;
        }

        return false;
    }

    public boolean CanBan(AbstractCard card)
    {
        if (counter > 0)
        {
            AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
            if (c != null)
            {
                Synergy s = c.GetSynergy();
                if (s != null && !s.Equals(Synergies.Kancolle) && !s.Equals(Synergies.HatarakuMaouSama))
                {
                    return !bannedSynergies.contains(s);
                }
            }
        }

        return false;
    }

    public void Ban(AbstractCard card)
    {
        AnimatorCard animatorCard = Utilities.SafeCast(card, AnimatorCard.class);
        if (animatorCard != null)
        {
            Synergy synergy = animatorCard.GetSynergy();
            if (synergy != null)
            {
                RemoveSynergy(synergy);
                bannedSynergies.add(synergy);

                counter -= 1;
                this.flash();
            }
        }
    }

    @Override
    public String onSave()
    {
        ArrayList<String> result = new ArrayList<>();
        for (Synergy s : bannedSynergies)
        {
            result.add(String.valueOf(s.ID));
        }

        return String.join("\u001F", result);
    }

    @Override
    public void onLoad(String value)
    {
        ArrayList<String> synergies = new ArrayList<>();
        Collections.addAll(synergies, value.split("\u001F"));
        for (String s : synergies)
        {
            Integer id;
            try
            {
                id = Integer.parseInt(s);
            }
            catch (NumberFormatException e)
            {
                id = null;
            }

            if (id != null)
            {
                bannedSynergies.add(Synergies.GetByID(id));
            }
        }

        AddUses(0);
    }

    private void RemoveSynergy(Synergy synergy)
    {
        if (synergy == null)
        {
            return;
        }

        int banCount = 0;
        int srcBanCount = 0;

        CardGroup pool;
        CardGroup srcPool;
        ArrayList<AnimatorCard> cards = Synergies.GetCardsWithSynergy(synergy);
        for (AnimatorCard c : cards)
        {
            switch (c.rarity)
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
                pool.removeCard(c.cardID);
                banCount++;
            }

            if (srcPool != null)
            {
                srcPool.removeCard(c.cardID);
                srcBanCount++;
            }
        }

        logger.info("Banned " + synergy.NAME + " " + banCount + ", " + srcBanCount);
    }

    public void UpdateBannedCards()
    {
        logger.info("Banned " + bannedSynergies.size() + " Synergies:");
        for (Synergy s : bannedSynergies)
        {
            RemoveSynergy(s);
        }
    }
}
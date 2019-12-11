package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringJoiner;

public class PurgingStone_Series extends AnimatorRelic implements CustomSavable<String>
{
    public static final String ID = CreateFullID(PurgingStone_Series.class.getSimpleName());

    private static Field isBoss = null;
    private final ArrayList<Synergy> bannedSynergies;

    public PurgingStone_Series()
    {
        super(ID, CreateFullID("PurgingStone"), RelicTier.STARTER, LandingSound.SOLID);

        bannedSynergies = new ArrayList<>();

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

    public ArrayList<Synergy> GetBannedSeries()
    {
        return new ArrayList<>(bannedSynergies);
    }

    public int GetBannedCount()
    {
        return bannedSynergies.size();
    }

    private void UpdateBannedTip()
    {
        String message;
        if (bannedSynergies != null && bannedSynergies.size() > 0)
        {
            StringJoiner sj = new StringJoiner(" NL ");
            sj.add(DESCRIPTIONS[0]);
            sj.add("");
            sj.add(DESCRIPTIONS[1]);

            for (Synergy bannedSynergy : bannedSynergies)
            {
                StringJoiner java = new StringJoiner(" ");
                java.add("- ");
                for (String s : bannedSynergy.NAME.split(" "))
                {
                    java.add("#r" + s);
                }
                sj.add(java.toString());
            }

            message = sj.toString();
        }
        else
        {
            message = DESCRIPTIONS[0];
        }

        if (this.tips.size() > 0)
        {
            this.tips.get(0).body = message;
        }
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        counter = 0;
        UpdateBannedTip();

        AddUses(4);
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

    public static PurgingStone_Series GetInstance()
    {
        if (AbstractDungeon.player == null || AbstractDungeon.player.relics == null)
        {
            return null;
        }

        for (AbstractRelic r : AbstractDungeon.player.relics)
        {
            if (r instanceof PurgingStone_Series)
            {
                return (PurgingStone_Series)r;
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
        AnimatorCard animatorCard = JavaUtilities.SafeCast(card, AnimatorCard.class);
        if (animatorCard != null)
        {
            return IsBanned(animatorCard.GetSynergy());
        }

        return false;
    }

    public boolean CanActivate(RewardItem rewardItem)
    {
        if (!GameUtilities.InBattle() && rewardItem != null && rewardItem.type == RewardItem.RewardType.CARD)
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
            AnimatorCard c = JavaUtilities.SafeCast(card, AnimatorCard.class);
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
        AnimatorCard animatorCard = JavaUtilities.SafeCast(card, AnimatorCard.class);
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

        UpdateBannedTip();
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
        UpdateBannedTip();
    }
}
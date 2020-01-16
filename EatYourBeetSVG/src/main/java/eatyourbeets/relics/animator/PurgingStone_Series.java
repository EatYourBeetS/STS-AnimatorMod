package eatyourbeets.relics.animator;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringJoiner;

public class PurgingStone_Series extends AbstractPurgingStone implements CustomSavable<String>, Hidden
{
    public static final int DEFAULT_SERIES_SIZE = 8;
    public static final String ID = CreateFullID(PurgingStone_Series.class.getSimpleName());

    private final ArrayList<Synergy> bannedSynergies = new ArrayList<>();

    public PurgingStone_Series()
    {
        super(ID, CreateFullID("PurgingStone"), RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(DEFAULT_SERIES_SIZE);
    }

    @Override
    public void update()
    {
        super.update();

        if (HitboxRightClick.rightClicked.get(this.hb) && GR.UI.SeriesSelection.CanOpen())
        {
            // TODO: Logic
            GR.UI.SeriesSelection.Open(true);
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
        if (bannedSynergies.size() > 0)
        {
            StringJoiner sj = new StringJoiner(" NL ");
            sj.add(getUpdatedDescription());
            sj.add("");
            sj.add(DESCRIPTIONS[1]);

            for (Synergy synergy : GetAvailableSeries())
            {
                StringJoiner javaIsAGreatProgrammingLanguage = new StringJoiner(" ");

                javaIsAGreatProgrammingLanguage.add("- ");

                for (String s : synergy.Name.split(" "))
                {
                    javaIsAGreatProgrammingLanguage.add("#y" + s);
                }

                sj.add(javaIsAGreatProgrammingLanguage.toString());
            }

            message = sj.toString();
        }
        else
        {
            message = getUpdatedDescription();
        }

        if (this.tips.size() > 0)
        {
            this.tips.get(0).body = message;
        }

        counter = (mainSeries.size() - bannedSynergies.size());
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        UpdateBannedTip();
    }

    public boolean IsBanned(Synergy synergy)
    {
        return bannedSynergies.contains(synergy);
    }

    @Override
    public boolean IsBanned(AbstractCard card)
    {
        AnimatorCard animatorCard = JavaUtilities.SafeCast(card, AnimatorCard.class);
        if (animatorCard != null)
        {
            return IsBanned(animatorCard.synergy);
        }

        return false;
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
        ArrayList<AnimatorCard> cards = Synergies.GetNonColorlessCard(synergy);
        for (AnimatorCard c : cards)
        {
            switch (c.rarity)
            {
                case COMMON:
                    srcPool = AbstractDungeon.srcCommonCardPool;
                    pool    = AbstractDungeon.commonCardPool;
                    break;

                case UNCOMMON:
                    srcPool = AbstractDungeon.srcUncommonCardPool;
                    pool    = AbstractDungeon.uncommonCardPool;
                    break;

                case RARE:
                    srcPool = AbstractDungeon.srcRareCardPool;
                    pool    = AbstractDungeon.rareCardPool;
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

        logger.info("Banned " + synergy.Name + " " + banCount + ", " + srcBanCount);
    }

    @Override
    public void UpdateBannedCardsInternal()
    {
        if (bannedSynergies.size() == 0)
        {
            flash();

            RandomizedList<Synergy> synergies = new RandomizedList<>(mainSeries.keySet());
            while (synergies.Count() > DEFAULT_SERIES_SIZE)
            {
                Synergy synergy = synergies.Retrieve(AbstractDungeon.miscRng);
                if (synergy.ID != GR.Animator.Database.SelectedLoadout.ID)
                {
                    bannedSynergies.add(synergy);
                }
            }
        }

        logger.info("Banned " + bannedSynergies.size() + " Synergies:");
        for (Synergy s : bannedSynergies)
        {
            RemoveSynergy(s);
        }

        UpdateBannedTip();
    }
}
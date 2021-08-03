package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.ImprovedDefend;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.*;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TupleT2;

import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class AnimatorLoadout
{
    public static class Validation
    {
        public final TupleT2<Integer, Boolean> CardsCount = new TupleT2<>();
        public final TupleT2<Integer, Boolean> TotalValue = new TupleT2<>();
        public int AffinityLevel;
        public int GoldValue;
        public int HpValue;
        public boolean AllCardsSeen;
        public boolean IsValid;

        public Validation()
        {

        }

        public Validation(AnimatorLoadoutData data)
        {
            Refresh(data);
        }

        public Validation Refresh(AnimatorLoadoutData data)
        {
            CardsCount.Set(0, false);
            TotalValue.Set(MAX_VALUE, false);
            AllCardsSeen = true;
            final EYBCardAffinities affinities = new EYBCardAffinities(null);
            for (CardSlot slot : data)
            {
                if (slot == null)
                {
                    continue;
                }

                affinities.Add(slot.GetAffinities(), 1);
                TotalValue.V1 += slot.GetEstimatedValue();
                CardsCount.V1 += slot.amount;

                if (slot.selected != null && slot.selected.data.IsNotSeen())
                {
                    AllCardsSeen = false;
                }
            }

            AffinityLevel = 0;
            for (AffinityType t : AffinityType.BasicTypes())
            {
                int level = affinities.GetLevel(t, false);
                if (level > 2)
                {
                    AffinityLevel += (level - 2);
                }
            }

            GoldValue = 2 * ((data.Gold - BASE_GOLD) / GOLD_STEP);
            HpValue = 2 * ((data.HP - BASE_HP) / HP_STEP);
            TotalValue.V1 += GoldValue + HpValue + AffinityLevel;
            TotalValue.V2 = TotalValue.V1 <= MAX_VALUE;
            CardsCount.V2 = CardsCount.V1 >= MIN_CARDS;
            IsValid = TotalValue.V2 && CardsCount.V2 && AllCardsSeen;

            return this;
        }
    }

    public static final int GOLD_HP_EDITOR_ASCENSION_REQUIRED = 7;
    public static final int MAX_VALUE = 30;
    public static final int MIN_CARDS = 10;
    public static final int BASE_GOLD = 99;
    public static final int BASE_HP = 60;
    public static final int GOLD_STEP = 30;
    public static final int HP_STEP = 4;
    public static final int MAX_STEP = 3;

    public AnimatorLoadoutData Data = new AnimatorLoadoutData();
    public CardSlot SpecialSlot1;
    public CardSlot SpecialSlot2;

    protected ArrayList<String> startingDeck = new ArrayList<>();
    protected String shortDescription = "";

    public int ID;
    public String Name;
    public CardSeries Series;
    public boolean IsBeta;

    public int CardDraw = 5;
    public int OrbSlots = 3;
    public int UnlockLevel = 0;

    public AnimatorLoadout(String name)
    {
        this.IsBeta = true;
        this.Series = null;
        this.Name = name;
        this.ID = -1;
    }

    public AnimatorLoadout(CardSeries series)
    {
        this.IsBeta = false;
        this.Series = series;
        this.Name = series.LocalizedName;
        this.ID = series.ID;
    }

    public abstract EYBCardData GetSymbolicCard();
    public abstract EYBCardData GetUltraRare();

    public void InitializeData()
    {
        Data.HP = BASE_HP;
        Data.Gold = BASE_GOLD;
        Data.AddSlot(1, 6).AddItem(Strike.DATA, -2);
        Data.AddSlot(1, 6).AddItem(Defend.DATA, -2);
        Data.AddSlot(0, 1).AddItems(ImprovedStrike.GetCards(), 1);
        Data.AddSlot(0, 1).AddItems(ImprovedDefend.GetCards(), 1);
        SpecialSlot1 = Data.AddSlot(0, 1);
        SpecialSlot2 = Data.AddSlot(0, 1);
    }

    public void LoadDefaultData()
    {
        if (Data.Ready)
        {
            return;
        }

        Data.HP = BASE_HP;
        Data.Gold = BASE_GOLD;
        Data.Get(0).Select(0, 4).GetData().MarkSeen();
        Data.Get(1).Select(0, 4).GetData().MarkSeen();
        Data.Get(2).Select(null);
        JUtils.ForEach(ImprovedStrike.GetCards(), EYBCardData::MarkSeen);
        Data.Get(3).Select(null);
        JUtils.ForEach(ImprovedDefend.GetCards(), EYBCardData::MarkSeen);
        Data.Get(4).Select(0, 1).GetData().MarkSeen();
        Data.Get(5).Select(1, 1).GetData().MarkSeen();
        Data.Ready = true;
    }

    public CharSelectInfo GetLoadout(String name, String description, AnimatorCharacter c)
    {
        return new CharSelectInfo(name + "-" + ID, description, Data.HP, Data.HP, OrbSlots, Data.Gold, CardDraw, c, GetStartingRelics(), GetStartingDeck(), false);
    }

    public ArrayList<String> GetStartingDeck()
    {
        final ArrayList<String> cards = new ArrayList<>();
        for (CardSlot slot : Data)
        {
            EYBCardData data = slot.GetData();
            if (data != null)
            {
                for (int i = 0; i < slot.amount; i++)
                {
                    cards.add(data.ID);
                }
            }
        }

        if (cards.isEmpty())
        {
            JUtils.LogWarning(this, "Starting loadout was empty");
            for (int i = 0; i < 5; i++)
            {
                cards.add(Strike.DATA.ID);
                cards.add(Defend.DATA.ID);
            }
        }

        return cards;
    }

    public ArrayList<String> GetStartingRelics()
    {
        if (!UnlockTracker.isRelicSeen(LivingPicture.ID))
        {
            UnlockTracker.markRelicAsSeen(LivingPicture.ID);
        }
        if (!UnlockTracker.isRelicSeen(TheMissingPiece.ID))
        {
            UnlockTracker.markRelicAsSeen(TheMissingPiece.ID);
        }

        ArrayList<String> res = new ArrayList<>();
        res.add(LivingPicture.ID);
        res.add(TheMissingPiece.ID);
        return res;
    }

    public int GetHP()
    {
        return Data.HP;
    }

    public int GetGold()
    {
        return Data.Gold;
    }

    public AnimatorTrophies GetTrophies()
    {
        if (IsBeta)
        {
            return null;
        }

        AnimatorTrophies trophies = GR.Animator.Data.GetTrophies(ID);
        if (trophies == null)
        {
            trophies = new AnimatorTrophies(ID);
            GR.Animator.Data.Trophies.add(trophies);
        }

        return trophies;
    }

    public String GetDeckPreviewString(boolean forceRefresh)
    {
        if (shortDescription == null || forceRefresh)
        {
            StringJoiner sj = new StringJoiner(", ");
            for (String s : GetStartingDeck())
            {
                AbstractCard card = CardLibrary.getCard(s);
                if (card.rarity != AbstractCard.CardRarity.BASIC)
                {
                    sj.add(card.originalName);
                }
            }

            shortDescription = sj.toString();
        }

        return shortDescription.isEmpty() ? "-" : shortDescription;
    }

    public String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return GR.Animator.Strings.Trophies.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return GR.Animator.Strings.Trophies.SilverDescription;
        }
        else if (trophy == 3)
        {
            return GR.Animator.Strings.Trophies.GoldDescription;
        }

        return null;
    }

    public void OnVictory(AnimatorLoadout currentLoadout, int ascensionLevel)
    {
        AnimatorTrophies trophies = GetTrophies();

        if (GR.Animator.Data.SelectedLoadout.ID == ID)
        {
            trophies.Trophy1 = Math.max(trophies.Trophy1, ascensionLevel);
        }

        ArrayList<String> cardsWithSynergy = new ArrayList<>();
        int synergyCount = 0;
        int uniqueCards = 0;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : cards)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                CardSeries series = card.series;
                if (series != null && series.ID == ID)
                {
                    synergyCount += 1;
                    if (!cardsWithSynergy.contains(card.cardID) && card.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        uniqueCards += 1;
                        cardsWithSynergy.add(card.cardID);
                    }
                }
            }
        }

        if (synergyCount >= cards.size() / 2)
        {
            trophies.Trophy2 = Math.max(trophies.Trophy2, ascensionLevel);
        }

        if (uniqueCards >= 8)
        {
            trophies.Trophy3 = Math.max(trophies.Trophy3, ascensionLevel);
        }
    }

    public Validation Validate()
    {
        return new Validation(Data);
    }

    protected void AddToSpecialSlots(EYBCardData data, int estimatedValue)
    {
        SpecialSlot1.AddItem(data, estimatedValue);
        SpecialSlot2.AddItem(data, estimatedValue);
    }
}
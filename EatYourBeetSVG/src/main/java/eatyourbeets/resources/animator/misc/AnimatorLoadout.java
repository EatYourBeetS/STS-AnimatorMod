package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
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
import eatyourbeets.interfaces.markers.Hidden;
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

        public static Validation For(AnimatorLoadoutData data)
        {
            return new Validation(data);
        }

        public Validation()
        {

        }

        public Validation(AnimatorLoadoutData data)
        {
            Refresh(data);
        }

        public Validation Refresh(AnimatorLoadoutData data)
        {
            if (data == null || data.Preset < 0 || data.Preset >= MAX_PRESETS)
            {
                IsValid = false;
                return this;
            }

            CardsCount.Set(0, false);
            TotalValue.Set(MAX_VALUE, false);
            AllCardsSeen = true;
            final EYBCardAffinities affinities = new EYBCardAffinities(null);
            for (AnimatorCardSlot slot : data)
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
            for (Affinity t : Affinity.Extended())
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

    public static final int GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED = 7;
    public static final int BRONZE_REQUIRED_PRESET_SLOT_2 = 7;
    public static final int BRONZE_REQUIRED_PRESET_SLOT_3 = 14;
    public static final int BRONZE_REQUIRED_EXPANSION = 16;
    public static final int MAX_PRESETS = 3;
    public static final int MAX_VALUE = 30;
    public static final int MIN_CARDS = 10;
    public static final int BASE_GOLD = 99;
    public static final int BASE_HP = 70;
    public static final int GOLD_STEP = 15;
    public static final int HP_STEP = 2;
    public static final int MAX_STEP = 6;

    public AnimatorLoadoutData[] Presets = new AnimatorLoadoutData[3];
    public AnimatorCardSlot SpecialSlot1;
    public AnimatorCardSlot SpecialSlot2;

    protected ArrayList<TupleT2<EYBCardData, Integer>> starterCards = new ArrayList<>();
    protected ArrayList<String> startingDeck = new ArrayList<>();
    protected String shortDescription = "";

    public int ID;
    public int Preset;
    public String Name;
    public CardSeries Series;
    public boolean IsBeta;
    public boolean HasExpansion;

    public int CardDraw = 5;
    public int OrbSlots = 3;
    public int UnlockLevel = 0;

    public AnimatorLoadout(String name)
    {
        this.IsBeta = true;
        this.Series = null;
        this.Name = name;
        this.ID = -1;
        this.HasExpansion = CheckForExpansion();
    }

    public AnimatorLoadout(CardSeries series)
    {
        this.IsBeta = false;
        this.Series = series;
        this.Name = series.LocalizedName;
        this.ID = series.ID;
        this.HasExpansion = CheckForExpansion();
    }

    private boolean CheckForExpansion() {
        if (this.Series == null) {
            return false;
        }
        for (AbstractCard c : CardLibrary.getAllCards())
        {
            AnimatorCard aC = JUtils.SafeCast(c, AnimatorCard.class);
            if (aC != null && this.Series.equals(aC.series) && aC.cardData != null && aC.cardData.IsExpansionCard && !(aC instanceof Hidden)) {
                return true;
            }
        }
        return false;
    }

    public abstract void AddStarterCards();
    public abstract EYBCardData GetSymbolicCard();
    public abstract EYBCardData GetUltraRare();

    public void InitializeData(AnimatorLoadoutData data)
    {
        data.HP = BASE_HP;
        data.Gold = BASE_GOLD;
        data.AddCardSlot(1, 6).AddItem(Strike.DATA, -2);
        data.AddCardSlot(1, 6).AddItem(Defend.DATA, -2);
        data.AddCardSlot(0, 1).AddItems(ImprovedStrike.GetCards(), 0);
        data.AddCardSlot(0, 1).AddItems(ImprovedDefend.GetCards(), 0);

        final AnimatorCardSlot s1 = data.AddCardSlot(0, 1);
        final AnimatorCardSlot s2 = data.AddCardSlot(0, 1);

        if (starterCards.isEmpty())
        {
            AddStarterCards();
        }

        for (TupleT2<EYBCardData, Integer> pair : starterCards)
        {
            s1.AddItem(pair.V1, pair.V2);
            s2.AddItem(pair.V1, pair.V2);
        }
    }

    public Validation Validate()
    {
        return GetPreset().Validate();
    }

    public boolean CanEnableExpansion()
    {
        if (!HasExpansion) {
            return false;
        }
        if (Settings.isDebug) {
            return true;
        }
        final AnimatorTrophies trophies = GetTrophies();
        final int bronze = trophies == null ? 20 : trophies.Trophy1;
        return bronze >= BRONZE_REQUIRED_EXPANSION;
    }

    public boolean CanChangePreset(int preset)
    {
        if (preset == 0)
        {
            return true;
        }
        else if (preset < 0 || preset >= MAX_PRESETS)
        {
            return false;
        }

        final AnimatorTrophies trophies = GetTrophies();
        final int bronze = trophies == null ? 20 : trophies.Trophy1;
        return (preset == 1) ? bronze >= BRONZE_REQUIRED_PRESET_SLOT_2 : bronze >= BRONZE_REQUIRED_PRESET_SLOT_3;
    }

    public AnimatorLoadoutData GetPreset()
    {
        return GetPreset(Preset);
    }

    public AnimatorLoadoutData GetPreset(int preset)
    {
        final AnimatorLoadoutData data = Presets[preset];
        if (data != null)
        {
            return data;
        }

        return Presets[preset] = GetDefaultData(preset);
    }

    public CharSelectInfo GetLoadout(String name, String description, AnimatorCharacter c)
    {
        final AnimatorLoadoutData data = GetPreset();
        return new CharSelectInfo(name + "-" + ID, description, data.HP, data.HP, OrbSlots, data.Gold, CardDraw, c, GetStartingRelics(), GetStartingDeck(), false);
    }

    public ArrayList<String> GetStartingDeck()
    {
        final ArrayList<String> cards = new ArrayList<>();
        for (AnimatorCardSlot slot : GetPreset())
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
        final ArrayList<String> res = new ArrayList<>();

        if (!UnlockTracker.isRelicSeen(LivingPicture.ID))
        {
            UnlockTracker.markRelicAsSeen(LivingPicture.ID);
        }
        res.add(LivingPicture.ID);

        if (!UnlockTracker.isRelicSeen(TheMissingPiece.ID))
        {
            UnlockTracker.markRelicAsSeen(TheMissingPiece.ID);
        }
        res.add(TheMissingPiece.ID);

        return res;
    }

    public int GetHP()
    {
        return GetPreset().HP;
    }

    public int GetGold()
    {
        return GetPreset().Gold;
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
            final StringJoiner sj = new StringJoiner(", ");
            for (String s : GetStartingDeck())
            {
                AbstractCard card = CardLibrary.getCard(s);
                if (card.rarity != AbstractCard.CardRarity.BASIC)
                {
                    sj.add(card.originalName);
                }
            }

            shortDescription = JUtils.Format("{0} #{1}",Series.LocalizedName,Preset + 1);
        }

        return shortDescription;
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

    protected AnimatorLoadoutData GetDefaultData(int preset)
    {
        final AnimatorLoadoutData data = new AnimatorLoadoutData(this);
        data.Preset = preset;
        data.HP = BASE_HP;
        data.Gold = BASE_GOLD;
        data.GetCardSlot(0).Select(0, 4).GetData().MarkSeen();
        data.GetCardSlot(1).Select(0, 4).GetData().MarkSeen();
        data.GetCardSlot(2).Select(null);
        JUtils.ForEach(ImprovedStrike.GetCards(), EYBCardData::MarkSeen);
        data.GetCardSlot(3).Select(null);
        JUtils.ForEach(ImprovedDefend.GetCards(), EYBCardData::MarkSeen);
        data.GetCardSlot(4).Select(0, 1).GetData().MarkSeen();
        data.GetCardSlot(5).Select(1, 1).GetData().MarkSeen();
        return data;
    }

    protected void AddStarterCard(EYBCardData data, Integer value)
    {
        starterCards.add(new TupleT2<>(data, value));
    }
}
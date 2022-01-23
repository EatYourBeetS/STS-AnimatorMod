package pinacolada.resources.pcl.misc;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.TupleT2;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.basic.Defend;
import pinacolada.cards.pcl.basic.Strike;
import pinacolada.cards.pcl.curse.*;
import pinacolada.characters.FoolCharacter;
import pinacolada.relics.PCLRelic;
import pinacolada.relics.pcl.*;
import pinacolada.resources.GR;
import pinacolada.ui.characterSelection.PCLBaseStatEditor;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import static pinacolada.ui.characterSelection.PCLLoadoutEditor.MAX_RELIC_SLOTS;

public abstract class PCLLoadout
{
    public static class Validation
    {
        public final TupleT2<Integer, Boolean> CardsCount = new TupleT2<>();
        public final TupleT2<Integer, Boolean> TotalValue = new TupleT2<>();
        public final HashMap<PCLBaseStatEditor.StatType, Integer> Values = new HashMap<>();
        public int HindranceLevel;
        public int AffinityLevel;
        public boolean AllCardsSeen;
        public boolean IsValid;

        public static Validation For(PCLLoadoutData data)
        {
            return new Validation(data);
        }

        public Validation()
        {

        }

        public Validation(PCLLoadoutData data)
        {
            Refresh(data);
        }

        public Validation Refresh(PCLLoadoutData data)
        {
            if (data == null || data.Preset < 0 || data.Preset >= MAX_PRESETS)
            {
                IsValid = false;
                return this;
            }

            CardsCount.Set(0, false);
            TotalValue.Set(MAX_VALUE, false);
            AllCardsSeen = true;
            int weakHindrances = 0;
            int strongHindrances = 0;
            final PCLCardAffinities affinities = new PCLCardAffinities(null);
            for (PCLCardSlot slot : data.cardSlots)
            {
                if (slot == null)
                {
                    continue;
                }

                affinities.Add(slot.GetAffinities(), 1);
                TotalValue.V1 += slot.GetEstimatedValue();
                CardsCount.V1 += slot.amount;

                if (slot.selected != null)
                {
                    if (slot.selected.data.IsNotSeen()) {
                        AllCardsSeen = false;
                    }

                    if (slot.selected.estimatedValue < -2) {
                        strongHindrances += slot.amount;
                    }
                    else if (slot.selected.estimatedValue < 0) {
                        weakHindrances += slot.amount;
                    }
                }
            }
            for (PCLRelicSlot slot : data.relicSlots)
            {
                if (slot == null)
                {
                    continue;
                }

                TotalValue.V1 += slot.GetEstimatedValue();
            }


            // Affinity level is determined by how easy it is to rack up a particular Affinity
            AffinityLevel = 0;
            for (PCLAffinity t : PCLAffinity.All())
            {
                int level = affinities.GetLevel(t, false);
                if (level > 2)
                {
                    AffinityLevel += (level - 2);
                }
            }

            // Hindrance level is determined by the proportion of your deck that is "bad"
            // Strikes/Defends and harmless hindrances have a weaker influence
            // Curses and damaging hindrances have a stronger influence
            int strongHindranceLevel = (int) Math.max(0, (30 * Math.pow(strongHindrances, 1.5) / CardsCount.V1) - 7);
            int weakHindranceLevel = Math.max(0, (30 * (weakHindrances + strongHindrances) / CardsCount.V1) - 20);
            HindranceLevel = -strongHindranceLevel -weakHindranceLevel;

            Values.putAll(data.Values);
            TotalValue.V1 += (int) PCLJUtils.Sum(Values.values(), Float::valueOf) + AffinityLevel + HindranceLevel;
            TotalValue.V2 = TotalValue.V1 <= MAX_VALUE;
            CardsCount.V2 = CardsCount.V1 >= MIN_CARDS;
            IsValid = TotalValue.V2 && CardsCount.V2 && AllCardsSeen;

            return this;
        }
    }

    public static final int GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED = 7;
    public static final int BRONZE_REQUIRED_EXPANSION = 16;
    public static final int GOLD_REQUIRED_UR = 18; //TODO implement the condition for this
    public static final int MAX_PRESETS = 5;
    public static final int MAX_VALUE = 35;
    public static final int MIN_CARDS = 10;

    public PCLLoadoutData[] Presets = new PCLLoadoutData[PCLLoadout.MAX_PRESETS];
    public PCLCardSlot SpecialSlot1;
    public PCLCardSlot SpecialSlot2;

    protected ArrayList<TupleT2<PCLCardData, Integer>> starterCards = new ArrayList<>();
    protected ArrayList<String> startingDeck = new ArrayList<>();
    protected String shortDescription = "";

    public int ID;
    public int Preset;
    public String Name;
    public CardSeries Series;
    public boolean IsBeta;
    public boolean HasExpansion;

    public int CardDraw = 5;
    public int UnlockLevel = 0;

    public PCLLoadout(String name)
    {
        this.IsBeta = true;
        this.Series = null;
        this.Name = name;
        this.ID = -1;
        this.HasExpansion = CheckForExpansion();
    }

    public PCLLoadout(CardSeries series)
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
            PCLCard aC = PCLJUtils.SafeCast(c, PCLCard.class);
            if (aC != null && this.Series.equals(aC.series) && aC.cardData != null && aC.cardData.IsExpansionCard && !(aC instanceof Hidden)) {
                return true;
            }
        }
        return false;
    }

    public abstract void AddStarterCards();
    public abstract PCLCardData GetSymbolicCard();
    public abstract PCLCardData GetUltraRare();

    public void AddLoadoutCards() {
        AddStarterCards();
        AddStarterCard(Curse_Clumsy.DATA, -3);
        AddStarterCard(Curse_Injury.DATA, -5);
        AddStarterCard(Curse_Writhe.DATA, -5);
        AddStarterCard(Curse_SearingBurn.DATA, -6);
        AddStarterCard(Curse_Depression.DATA, -7);
        AddStarterCard(Curse_Greed.DATA, -7);
        AddStarterCard(Curse_Parasite.DATA, -7);
        AddStarterCard(Curse_Doubt.DATA, -7);
        AddStarterCard(Curse_Decay.DATA, -7);
        AddStarterCard(Curse_Shame.DATA, -7);
        AddStarterCard(Curse_Regret.DATA, -9);
        AddStarterCard(Curse_Pain.DATA, -10);
    }

    public void InitializeData(PCLLoadoutData data)
    {
        for (PCLBaseStatEditor.StatType type : PCLBaseStatEditor.StatType.values()) {
            data.Values.put(type, 0);
        }
        data.AddCardSlot(1, PCLCardSlot.MAX_LIMIT).AddItem(Strike.DATA, -2);
        data.AddCardSlot(1, PCLCardSlot.MAX_LIMIT).AddItem(Defend.DATA, -2);

        final PCLCardSlot s1 = data.AddCardSlot(0, PCLCardSlot.MAX_LIMIT);
        final PCLCardSlot s2 = data.AddCardSlot(0, PCLCardSlot.MAX_LIMIT);
        final PCLCardSlot s3 = data.AddCardSlot(0, PCLCardSlot.MAX_LIMIT);
        final PCLCardSlot s4 = data.AddCardSlot(0, PCLCardSlot.MAX_LIMIT);

        if (starterCards.isEmpty())
        {
            AddLoadoutCards();
        }

        for (TupleT2<PCLCardData, Integer> pair : starterCards)
        {
            s1.AddItem(pair.V1, pair.V2);
            s2.AddItem(pair.V1, pair.V2);
            s3.AddItem(pair.V1, pair.V2);
            s4.AddItem(pair.V1, pair.V2);
        }

        for (int i = 0; i < MAX_RELIC_SLOTS; i++) {
            PCLRelicSlot r1 = data.AddRelicSlot();
            r1.AddItem(new PolychromePaintbrush(), 2);
            r1.AddItem(new SpitefulCubes(), 3);
            r1.AddItem(new ConcertsFinalHour(), 5);
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
        final PCLTrophies trophies = GetTrophies();
        final int bronze = trophies == null ? 20 : trophies.Trophy1;
        return bronze >= BRONZE_REQUIRED_EXPANSION;
    }

    public boolean CanChangePreset(int preset)
    {
        if (preset < 0 || preset >= MAX_PRESETS)
        {
            return false;
        }
        return true;
    }

    public PCLLoadoutData GetPreset()
    {
        return GetPreset(Preset);
    }

    public PCLLoadoutData GetPreset(int preset)
    {
        final PCLLoadoutData data = Presets[preset];
        if (data != null)
        {
            return data;
        }

        return Presets[preset] = GetDefaultData(preset);
    }

    public CharSelectInfo GetLoadout(String name, String description, FoolCharacter c)
    {
        int hp = GetHP();
        return new CharSelectInfo(name + "-" + ID, description, hp, hp, GetOrbSlots(), GetGold(), CardDraw, c, GetStartingRelics(), GetStartingDeck(), false);
    }

    public ArrayList<String> GetStartingDeck()
    {
        final ArrayList<String> cards = new ArrayList<>();
        for (PCLCardSlot slot : GetPreset().cardSlots)
        {
            PCLCardData data = slot.GetData();
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
            PCLJUtils.LogWarning(this, "Starting loadout was empty");
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

        if (!UnlockTracker.isRelicSeen(UsefulBox.ID))
        {
            UnlockTracker.markRelicAsSeen(UsefulBox.ID);
        }
        res.add(UsefulBox.ID);

        if (!UnlockTracker.isRelicSeen(FoolishCubes.ID))
        {
            UnlockTracker.markRelicAsSeen(FoolishCubes.ID);
        }
        res.add(FoolishCubes.ID);

        for (PCLRelicSlot rSlot : GetPreset().relicSlots) {
            if (rSlot.selected != null && rSlot.selected.relic != null) {
                String relicID = rSlot.selected.relic.relicId;
                if (SpitefulCubes.ID.equals(relicID)) {
                    res.set(1, relicID);
                }
                else {
                    res.add(rSlot.selected.relic.relicId);
                }
            }
        }
        return res;
    }

    public int GetHP()
    {
        return PCLBaseStatEditor.StatType.HP.GetAmount(GetPreset());
    }

    public int GetGold()
    {
        return PCLBaseStatEditor.StatType.Gold.GetAmount(GetPreset());
    }

    public int GetPotionSlots()
    {
        return PCLBaseStatEditor.StatType.PotionSlot.GetAmount(GetPreset());
    }

    public int GetOrbSlots()
    {
        return PCLBaseStatEditor.StatType.OrbSlot.GetAmount(GetPreset());
    }

    public int GetCommonUpgrades()
    {
        return PCLBaseStatEditor.StatType.CommonUpgrade.GetAmount(GetPreset());
    }

    public PCLTrophies GetTrophies()
    {
        if (IsBeta)
        {
            return null;
        }

        PCLTrophies trophies = GR.PCL.Data.GetTrophies(ID);
        if (trophies == null)
        {
            trophies = new PCLTrophies(ID);
            GR.PCL.Data.Trophies.add(trophies);
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

            shortDescription = PCLJUtils.Format("{0} #{1}",Series.LocalizedName,Preset + 1);
        }

        return shortDescription;
    }

    public String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return GR.PCL.Strings.Trophies.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return GR.PCL.Strings.Trophies.SilverDescription;
        }
        else if (trophy == 3)
        {
            return GR.PCL.Strings.Trophies.GoldDescription;
        }

        return null;
    }

    public void OnVictory(int ascensionLevel, int trophyLevel)
    {
        PCLTrophies trophies = GetTrophies();
        if (GR.PCL.Data.SelectedLoadout.ID == ID)
        {
            switch (trophyLevel) {
                case 2:
                    trophies.Trophy2 = Math.max(trophies.Trophy2, ascensionLevel);
                    break;
                case 3:
                    trophies.Trophy3 = Math.max(trophies.Trophy3, ascensionLevel);
                    break;
            }
            trophies.Trophy1 = Math.max(trophies.Trophy1, ascensionLevel);
        }
    }

    public PCLLoadoutData GetDefaultData(int preset)
    {
        final PCLLoadoutData data = new PCLLoadoutData(this);
        data.Preset = preset;
        for (PCLBaseStatEditor.StatType type : PCLBaseStatEditor.StatType.values()) {
            data.Values.put(type, 0);
        }
        data.GetCardSlot(0).Select(0, 5).GetData().MarkSeen();
        data.GetCardSlot(1).Select(0, 5).GetData().MarkSeen();
        data.GetCardSlot(2).Select(0, 1).GetData().MarkSeen();
        data.GetCardSlot(3).Select(1, 1).GetData().MarkSeen();
        data.GetCardSlot(4).Select(null);
        data.GetCardSlot(5).Select(null);
        data.GetRelicSlot(0).Select((PCLRelic) null);
        data.GetRelicSlot(1).Select((PCLRelic) null);
        //data.GetCardSlot(2).Select(0, 1).GetData().MarkSeen();
        //data.GetCardSlot(3).Select(1, 1).GetData().MarkSeen();
        return data;
    }

    // TODO finish this
    public PCLLoadoutData RandomizeData(int preset)
    {
        final ArrayList<PCLCardSlot.Item> negativeItems = new ArrayList<>();
        final ArrayList<PCLCardSlot.Item> positiveItems = new ArrayList<>();
        final PCLLoadoutData data = new PCLLoadoutData(this);
        data.Preset = preset;

        for (PCLCardSlot.Item pick : data.GetCardSlot(2).Cards) {
            if (pick.estimatedValue < 0) {
                negativeItems.add(pick);
            }
            else {
                positiveItems.add(pick);
            }
        }
        negativeItems.sort((a, b) -> b.estimatedValue - a.estimatedValue);
        positiveItems.sort((a, b) -> b.estimatedValue - a.estimatedValue);

        /* Select, in order:
            -Strikes
            -Defends
            -negative cards (from highest to lowest)
            -positive cards (from highest to lowest)
            -relics
            -hp
            -gold
         */

        data.GetCardSlot(0).Select(0, MathUtils.random(2,6)).GetData().MarkSeen();
        data.GetCardSlot(1).Select(0, MathUtils.random(2,6)).GetData().MarkSeen();

        // Favor less negative items over more ngeative items
        for (PCLCardSlot.Item negativeItem : negativeItems) {
            int amount = MathUtils.random(-2,2);
        }

        // TODO finish me
        return data;
    }

    protected void AddStarterCard(PCLCardData data, Integer value)
    {
        starterCards.add(new TupleT2<>(data, value));
        //data.MarkSeen();
    }
}
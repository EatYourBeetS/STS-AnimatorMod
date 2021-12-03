package eatyourbeets.resources.animator.misc;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.*;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.PurgingStone;
import eatyourbeets.relics.animator.RollingCubes;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.relics.animator.beta.PolychromePaintbrush;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.characterSelection.AnimatorBaseStatEditor;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TupleT2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import static eatyourbeets.ui.animator.characterSelection.AnimatorLoadoutEditor.MAX_RELIC_SLOTS;

public abstract class AnimatorLoadout
{
    public static class Validation
    {
        public final TupleT2<Integer, Boolean> CardsCount = new TupleT2<>();
        public final TupleT2<Integer, Boolean> TotalValue = new TupleT2<>();
        public final HashMap<AnimatorBaseStatEditor.StatType, Integer> Values = new HashMap<>();
        public int HindranceLevel;
        public int AffinityLevel;
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
            int weakHindrances = 0;
            int strongHindrances = 0;
            final EYBCardAffinities affinities = new EYBCardAffinities(null);
            for (AnimatorCardSlot slot : data.cardSlots)
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
            for (AnimatorRelicSlot slot : data.relicSlots)
            {
                if (slot == null)
                {
                    continue;
                }

                TotalValue.V1 += slot.GetEstimatedValue();
            }


            // Affinity level is determined by how easy it is to rack up a particular Affinity
            AffinityLevel = 0;
            for (Affinity t : Affinity.All())
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
            int strongHindranceLevel = Math.max(0, (21 * strongHindrances * strongHindrances / CardsCount.V1));
            int weakHindranceLevel = Math.max(0, (30 * (weakHindrances + strongHindrances) / CardsCount.V1) - 20);
            HindranceLevel = -strongHindranceLevel -weakHindranceLevel;

            Values.putAll(data.Values);
            TotalValue.V1 += (int) JUtils.Sum(Values.values(), Float::valueOf) + AffinityLevel + HindranceLevel;
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
        for (AnimatorBaseStatEditor.StatType type : AnimatorBaseStatEditor.StatType.values()) {
            data.Values.put(type, 0);
        }
        data.AddCardSlot(1, AnimatorCardSlot.MAX_LIMIT).AddItem(Strike.DATA, -2);
        data.AddCardSlot(1, AnimatorCardSlot.MAX_LIMIT).AddItem(Defend.DATA, -2);

        final AnimatorCardSlot s1 = data.AddCardSlot(0, AnimatorCardSlot.MAX_LIMIT);
        final AnimatorCardSlot s2 = data.AddCardSlot(0, AnimatorCardSlot.MAX_LIMIT);
        final AnimatorCardSlot s3 = data.AddCardSlot(0, AnimatorCardSlot.MAX_LIMIT);
        final AnimatorCardSlot s4 = data.AddCardSlot(0, AnimatorCardSlot.MAX_LIMIT);

        if (starterCards.isEmpty())
        {
            AddStarterCards();
        }

        for (TupleT2<EYBCardData, Integer> pair : starterCards)
        {
            s1.AddItem(pair.V1, pair.V2);
            s2.AddItem(pair.V1, pair.V2);
            s3.AddItem(pair.V1, pair.V2);
            s4.AddItem(pair.V1, pair.V2);
        }

        for (int i = 0; i < MAX_RELIC_SLOTS; i++) {
            AnimatorRelicSlot r1 = data.AddRelicSlot();
            r1.AddItem(new RollingCubes(), 2);
            r1.AddItem(new PolychromePaintbrush(), 3);
            r1.AddItem(new PurgingStone(), 3);
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
        if (preset < 0 || preset >= MAX_PRESETS)
        {
            return false;
        }
        return true;
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
        int hp = GetHP();
        return new CharSelectInfo(name + "-" + ID, description, hp, hp, OrbSlots, GetGold(), CardDraw, c, GetStartingRelics(), GetStartingDeck(), false);
    }

    public ArrayList<String> GetStartingDeck()
    {
        final ArrayList<String> cards = new ArrayList<>();
        for (AnimatorCardSlot slot : GetPreset().cardSlots)
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

    public ArrayList<String> GetAdditionalRelics()
    {
        final ArrayList<String> relicList = new ArrayList<>();
        for (AnimatorRelicSlot rSlot : GetPreset().relicSlots) {
            if (rSlot.selected != null && rSlot.selected.relic != null) {
                relicList.add(rSlot.selected.relic.relicId);
            }
        }
        return relicList;
    }

    public int GetHP()
    {
        return AnimatorBaseStatEditor.StatType.HP.GetAmount(GetPreset());
    }

    public int GetGold()
    {
        return AnimatorBaseStatEditor.StatType.Gold.GetAmount(GetPreset());
    }

    public int GetCommonUpgrades()
    {
        return AnimatorBaseStatEditor.StatType.CommonUpgrade.GetAmount(GetPreset());
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

    public AnimatorLoadoutData GetDefaultData(int preset)
    {
        final AnimatorLoadoutData data = new AnimatorLoadoutData(this);
        data.Preset = preset;
        for (AnimatorBaseStatEditor.StatType type : AnimatorBaseStatEditor.StatType.values()) {
            data.Values.put(type, 0);
        }
        data.GetCardSlot(0).Select(0, 5).GetData().MarkSeen();
        data.GetCardSlot(1).Select(0, 5).GetData().MarkSeen();
        data.GetCardSlot(2).Select(null);
        data.GetCardSlot(3).Select(null);
        data.GetCardSlot(4).Select(null);
        data.GetCardSlot(5).Select(null);
        data.GetRelicSlot(0).Select((EYBRelic) null);
        data.GetRelicSlot(1).Select((EYBRelic) null);
        //data.GetCardSlot(2).Select(0, 1).GetData().MarkSeen();
        //data.GetCardSlot(3).Select(1, 1).GetData().MarkSeen();
        return data;
    }

    // TODO finish this
    public AnimatorLoadoutData RandomizeData(int preset)
    {
        final ArrayList<AnimatorCardSlot.Item> negativeItems = new ArrayList<>();
        final ArrayList<AnimatorCardSlot.Item> positiveItems = new ArrayList<>();
        final AnimatorLoadoutData data = new AnimatorLoadoutData(this);
        data.Preset = preset;

        for (AnimatorCardSlot.Item pick : data.GetCardSlot(2).Cards) {
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

        data.GetCardSlot(0).Select(0, MathUtils.random(3,6)).GetData().MarkSeen();
        data.GetCardSlot(1).Select(0, MathUtils.random(3,6)).GetData().MarkSeen();

        // Favor less negative items over more ngeative items
        for (AnimatorCardSlot.Item negativeItem : negativeItems) {
            int amount = MathUtils.random(-2,2);
        }

        // TODO finish me
        return data;
    }

    protected void AddStarterCard(EYBCardData data, Integer value)
    {
        starterCards.add(new TupleT2<>(data, value));
    }
}
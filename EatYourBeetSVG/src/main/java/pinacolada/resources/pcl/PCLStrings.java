package pinacolada.resources.pcl;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

public class PCLStrings
{
    public Rewards Rewards;
    public Misc Misc;
    public Series Series;
    public SeriesUI SeriesUI;
    public CharacterSelect CharSelect;
    public SeriesSelection SeriesSelection;
    public SeriesSelectionButtons SeriesSelectionButtons;
    public SingleCardPopupButtons SingleCardPopupButtons;
    public Actions Actions;
    public Trophies Trophies;
    public Tutorial Tutorial;
    public Combat Combat;
    public Hotkeys Hotkeys;
    public CardMods CardMods;
    public TheUnnamedReign TheUnnamedReign;
    public HandSelection HandSelection;
    public GridSelection GridSelection;

    public void Initialize()
    {
        Misc = new Misc();
        Rewards = new Rewards();
        Series = new Series();
        SeriesUI = new SeriesUI();
        CharSelect = new CharacterSelect();
        Actions = new Actions();
        Trophies = new Trophies();
        Tutorial = new Tutorial();
        SeriesSelection = new SeriesSelection();
        SeriesSelectionButtons = new SeriesSelectionButtons();
        SingleCardPopupButtons = new SingleCardPopupButtons();
        Hotkeys = new Hotkeys();
        Combat = new Combat();
        CardMods = new CardMods();
        TheUnnamedReign = new TheUnnamedReign();
        HandSelection = new HandSelection();
        GridSelection = new GridSelection();
    }

    public class Rewards
    {
        private final UIStrings Strings = GetUIStrings("Rewards");

        public final String Description = Strings.TEXT[0];
        public final String BonusRelic = Strings.TEXT[1];
        public final String CursedRelic = Strings.TEXT[2];
        public final String Reroll = Strings.TEXT[3];
        public final String MaxHPBonus_F1 = Strings.TEXT[4];
        public final String GoldBonus_F1 = Strings.TEXT[5];
        public final String CommonUpgrade = Strings.TEXT[6];
        public final String RightClickPreview = Strings.TEXT[7];
        public final String Enchantment = Strings.TEXT[8];

        public final String MaxHPBonus(int amount)
        {
            return PCLJUtils.Format(MaxHPBonus_F1, amount);
        }

        public final String GoldBonus(int amount)
        {
            return PCLJUtils.Format(GoldBonus_F1, amount);
        }
    }

    public class Series
    {
        private final UIStrings Strings = GetUIStrings("Series");

        public final String Series = Strings.EXTRA_TEXT[0];
        public final String RandomSeries = Strings.EXTRA_TEXT[1];
        public final String Colorless = Strings.EXTRA_TEXT[2];

        public final String SeriesName(int seriesID)
        {
            return Strings.TEXT.length > seriesID ? Strings.TEXT[seriesID] : null;
        }
    }

    public class SeriesUI
    {
        private final UIStrings Strings = GetUIStrings("SeriesUI");

        public final String SeriesUI = Strings.TEXT[0];
        public final String ItemsSelected = Strings.TEXT[1];
        public final String Keywords = Strings.TEXT[2];
        public final String Costs = Strings.TEXT[3];
        public final String Rarities = Strings.TEXT[4];
        public final String Types = Strings.TEXT[5];
        public final String Affinities = Strings.TEXT[6];
        public final String Scalings = Strings.TEXT[7];
        public final String Amount = Strings.TEXT[8];
    }

    public class Misc
    {
        private final UIStrings Strings = GetUIStrings("Misc");

        public final String Discord = Strings.TEXT[0];
        public final String DiscordDescription = Strings.TEXT[1];
        public final String Steam = Strings.TEXT[2];
        public final String SteamDescription = Strings.TEXT[3];
        public final String NotEnoughCards = Strings.TEXT[4];
        public final String DynamicPortraits = Strings.TEXT[5];
        public final String UseCardHoveringAnimation = Strings.TEXT[6];
        public final String PressToCycle = Strings.TEXT[7];
        public final String LocalizationHelp = Strings.TEXT[8];
        public final String DisplayBetaSeries = Strings.TEXT[9];
        public final String LocalizationHelpHeader = Strings.TEXT[10];
        public final String FadeCardsWithoutSynergy = Strings.TEXT[11];
        public final String SimplifyCardUI = Strings.TEXT[12];
        public final String RewardsDisabled = Strings.TEXT[13];
        public final String ConsoleDisabled = Strings.TEXT[14];
        public final String HealingWarning = Strings.TEXT[15];
        public final String MaxStacks_F1 = Strings.TEXT[16];
        public final String GainBlockAboveMaxStacks_F1 = Strings.TEXT[17];
        public final String MaxBlock_F1 = Strings.TEXT[18];
        public final String EnableEventsForOtherCharacters = Strings.TEXT[19];
        public final String EnableRelicsForOtherCharacters = Strings.TEXT[20];
        public final String UnofficialDisclaimer = Strings.TEXT[21];
        public final String Filters = Strings.TEXT[22];
        public final String Any = Strings.TEXT[23];
        public final String ReplaceCards = Strings.TEXT[24];

        public final String MaxStacks(int stacks)
        {
            return PCLJUtils.Format(MaxStacks_F1, stacks);
        }

        public final String GainBlockAboveMaxStacks(int stacks)
        {
            return PCLJUtils.Format(GainBlockAboveMaxStacks_F1, stacks);
        }

        public final String MaxBlock(int block)
        {
            return PCLJUtils.Format(MaxBlock_F1, block);
        }

        public final String PressKeyToCycle(String keyName) {
            return PCLJUtils.Format(PressToCycle, keyName);
        }
    }

    public class CharacterSelect
    {
        private final UIStrings Strings = GetUIStrings("CharacterSelect");

        public final String LeftText = Strings.TEXT[0];  // Starting Cards:
        public final String RightText = Strings.TEXT[1]; // Unlock
        public final String InvalidLoadout = Strings.TEXT[3];
        public final String DeckEditor = Strings.TEXT[5];
        public final String DeckEditorInfo = Strings.TEXT[6];
        public final String SeriesEditor = Strings.TEXT[8];
        public final String SeriesEditorInfo = Strings.TEXT[9];
        public final String DeckHeader = Strings.TEXT[10];
        public final String RelicsHeader = Strings.TEXT[11];
        public final String AttributesHeader = Strings.TEXT[12];
        public final String Clear = Strings.TEXT[13];

        public final String UnlocksAtLevel(int unlockLevel, int currentLevel)
        {
            return PCLJUtils.Format(Strings.TEXT[2], unlockLevel, currentLevel);
        }

        public final String UnlocksAtAscension(int ascension)
        {
            return PCLJUtils.Format(Strings.TEXT[4], ascension);
        }

        public final String ObtainBronzeAtAscension(int ascension)
        {
            return PCLJUtils.Format(Strings.TEXT[7], ascension);
        }

        public final String HindranceValue(int value)
        {
            return PCLJUtils.Format(Strings.TEXT[14], value);
        }

        public final String AffinityValue(int value)
        {
            return PCLJUtils.Format(Strings.TEXT[15], value);
        }

        public final String CardsCount(int value)
        {
            return PCLJUtils.Format(Strings.TEXT[16], value);
        }

        public final String TotalValue(int value, int max)
        {
            return PCLJUtils.Format(Strings.TEXT[17], value, max);
        }
    }

    public class SeriesSelection
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelection");

        public final String Beta = Strings.TEXT[1];
        public final String TooltipBeta = Strings.TEXT[2];
        public final String RemoveFromPool = Strings.TEXT[3];
        public final String AddToPool = Strings.TEXT[4];
        public final String ViewPool = Strings.TEXT[5];
        public final String RightClickToPreview = Strings.TEXT[6];
        public final String ExpansionHeader = Strings.TEXT[7];
        public final String ExpansionCardBody = Strings.TEXT[8];
        public final String ExpansionSeriesUnlocked = Strings.TEXT[9];
        public final String ExpansionSeriesLocked = Strings.TEXT[10];
        public final String PoolSizeHeader = Strings.TEXT[11];
        public final String PoolSizeTip = Strings.TEXT[12];
        public final String SeriesSelectedCount = Strings.TEXT[13];
        public final String CardsSelectedCount = Strings.TEXT[14];

        public final String CardsSelected(Object cardCount)
        {
            return PCLJUtils.Format(CardsSelectedCount, cardCount);
        }

        public final String ContainsNCards(Object cardCount)
        {
            return PCLJUtils.Format(Strings.TEXT[0], cardCount);
        }

        public final String ContainsNCards_Full(Object cardCount, Object ascensionLevel)
        {
            return ContainsNCards(cardCount) + " NL  NL " + GR.Tooltips.Trophy + ": " + ascensionLevel;
        }

        public final String ContainsNCards_Beta(Object cardCount)
        {
            return ContainsNCards(cardCount) + " NL " + Strings.TEXT[1] + ".";
        }

        public final String SeriesSelected(Object cardCount)
        {
            return PCLJUtils.Format(SeriesSelectedCount, cardCount);
        }
    }

    public class SeriesSelectionButtons
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelectionButtons");

        public final String ShowBetaSeries = Strings.TEXT[0];
        public final String DeselectAll = Strings.TEXT[1];
        public final String SelectAll = Strings.TEXT[2];
        public final String SelectRandom = Strings.TEXT[3];
        public final String ShowCardPool = Strings.TEXT[4];
        public final String Save = Strings.TEXT[5];
        public final String EnableExpansion = Strings.TEXT[6];
        public final String DisableExpansion = Strings.TEXT[7];
        public final String AllExpansionEnable = Strings.TEXT[8];
        public final String AllExpansionDisable = Strings.TEXT[9];
        public final String Cancel = Strings.TEXT[10];

        public final String SelectRandom(int cards)
        {
            return PCLJUtils.Format(Strings.TEXT[2], cards) ;
        }
    }

    public class SingleCardPopupButtons
    {
        private final UIStrings Strings = GetUIStrings("SingleCardPopupButtons");

        public final String Variant = Strings.TEXT[0];
        public final String ChangeVariant = Strings.TEXT[1];
        public final String ChangeVariantTooltipPermanent = Strings.TEXT[2];
        public final String ChangeVariantTooltipAlways = Strings.TEXT[3];
        public final String CurrentCopies = Strings.TEXT[4];
        public final String MaxCopies = Strings.TEXT[5];
        public final String MaxCopiesTooltip = Strings.TEXT[6];
    }

    public class Hotkeys
    {
        private final UIStrings Strings = GetUIStrings("Hotkeys");

        public final String ControlPileChange = Strings.TEXT[0];
        public final String ControlPileSelect = Strings.TEXT[1];
        public final String Cycle = Strings.TEXT[2];
        public final String RerollCurrent = Strings.TEXT[3];
        public final String RerollNext = Strings.TEXT[4];
    }

    public class Combat
    {
        private final UIStrings Strings = GetUIStrings("Combat");

        public final String Current = Strings.TEXT[0];
        public final String Next = Strings.TEXT[1];
        public final String Uses = Strings.TEXT[2];
        public final String Rerolls = Strings.TEXT[3];
        public final String Experience = Strings.TEXT[4];
        public final String ControlPile = Strings.TEXT[5];
        public final String ControlPileDescription = Strings.TEXT[6];
        public final String TotalMatches = Strings.TEXT[7];
        public final String CurrentMatchCombo = Strings.TEXT[8];
        public final String LongestMatchCombo = Strings.TEXT[9];

        public final String ControlPileDescriptionFull(String keyName) {
            return PCLJUtils.Format(ControlPileDescription, keyName);
        }
    }

    public class CardMods
    {
        private final UIStrings Strings = GetUIStrings("CardMods");

        public final String HandSize = Strings.TEXT[0];
        public final String Unknown = Strings.TEXT[1];
        public final String AfterLifeLocked = Strings.TEXT[2];
        public final String RespecLivingPicture = Strings.TEXT[3];
        public final String RespecLivingPictureLocked = Strings.TEXT[4];
        public final String RespecLivingPictureDescription = Strings.TEXT[5];
        public final String Kirby = Strings.TEXT[6];
        public final String KirbyDescription = Strings.TEXT[7];
    }

    public class Actions
    {
        private final UIStrings Strings = GetUIStrings("Actions");

        public final String GainAmount(Object amount, Object buff, boolean addPeriod)
        {
            return Format(addPeriod, 0, amount, buff);
        }

        public final String Apply(Object amount, Object debuff, boolean addPeriod)
        {
            return Format(addPeriod, 1, amount, debuff);
        }

        public final String ApplyToALL(Object amount, Object debuff, boolean addPeriod)
        {
            return Format(addPeriod, 2, amount, debuff);
        }

        public final String Cycle(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 3, amount);
        }

        public final String Draw(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 4, amount);
        }

        public final String Discard(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 5, amount);
        }

        public final String Exhaust(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 6, amount);
        }

        public final String GainOrbSlots(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 7, amount);
        }

        public final String ChannelRandomOrbs(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 8, amount);
        }

        public final String Channel(Object amount, Object orb, boolean addPeriod)
        {
            return Format(addPeriod, 9, amount, orb);
        }

        public final String Motivate(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 10, amount);
        }

        public final String UpgradeALLCardsInHand(boolean addPeriod)
        {
            return Get(11) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String GainTemporaryAmount(Object amount, Object buff, boolean addPeriod)
        {
            return Format(addPeriod, 12, amount, buff);
        }

        public final String RemoveALLDebuffs(boolean addPeriod)
        {
            return Get(13) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String PlayTopCard(boolean addPeriod)
        {
            return Get(14) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String Stun(boolean addPeriod)
        {
            return Get(15) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String EnterStance(Object stance, boolean addPeriod)
        {
            return Format(addPeriod, 16, stance);
        }

        public final String Boost(Object power, boolean addPeriod)
        {
            return Format(addPeriod, 17, power);
        }

        public final String Scry(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 18, amount);
        }

        public final String NextTurnBlock(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 19, amount);
        }

        public final String NextTurnDraw(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 20, amount);
        }

        public final String NextTurnEnergy(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 21, amount);
        }

        public final String EnterAnyStance(boolean addPeriod)
        {
            return Get(22) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String PayEnergy(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 23, amount);
        }

        public final String LoseHP(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 24, amount);
        }

        public final String GiveRandomEnemy(Object amount, Object debuff, boolean addPeriod)
        {
            return Format(addPeriod, 25, amount, debuff);
        }

        public final String GiveAllEnemies(Object amount, Object debuff, boolean addPeriod)
        {
            return Format(addPeriod, 26, amount, debuff);
        }

        public final String LosePower(Object amount, Object power, boolean addPeriod)
        {
            return Format(addPeriod, 27, amount, power);
        }

        public final String TakeDamage(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 28, amount);
        }

        public final String AddToDiscardPile(Object amount, Object card, boolean addPeriod)
        {
            return Format(addPeriod, 29, amount, card);
        }

        public final String AddToDrawPile(Object amount, Object card, boolean addPeriod)
        {
            return Format(addPeriod, 30, amount, card);
        }

        public final String AddToHand(Object amount, Object card, boolean addPeriod)
        {
            return Format(addPeriod, 31, amount, card);
        }

        public final String DiscardRandom(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 32, amount);
        }

        public final String ExhaustRandom(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 33, amount);
        }

        public final String NextTurnDrawLess(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 34, amount);
        }

        public final String NextTurnLoseEnergy(Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 35, amount);
        }

        public final String Play(Object card, boolean addPeriod)
        {
            return Format(addPeriod, 36, card);
        }

        public final String PlayFromAnywhere(Object card, boolean addPeriod)
        {
            return Format(addPeriod, 37, card);
        }

        public final String PlayOrbCore(boolean addPeriod)
        {
            return Get(38) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String PlayAffinityToken(boolean addPeriod)
        {
            return Get(39) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String ChooseMotivatedCard(Object category, boolean addPeriod)
        {
            return Format(addPeriod, 40, category);
        }

        public final String CreateCurses(boolean addPeriod)
        {
            return Get(41) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String HealHP(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 42, amount);
        }

        public final String Trigger(Object orb, Object amount, boolean addPeriod)
        {
            return Format(addPeriod, 43, orb, amount);
        }

        public final String PayCost(Object amount, Object buff, boolean addPeriod)
        {
            return Format(addPeriod, 44, amount, buff);
        }

        private String Format(boolean addPeriod, int index, Object amount)
        {
            return Format(addPeriod, index, amount, null);
        }

        private String Format(boolean addPeriod, int index, Object amount, Object extra)
        {
            String text = Strings.TEXT[index];
            if (amount instanceof Integer)
            {
                if (amount.equals(1))
                {
                    text = text.replace("(s)", "");
                }
                else
                {
                    text = text.replace("(s)", "s");
                }
            }

            return PCLJUtils.Format(text, amount, extra) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String Get(int index)
        {
            return Strings.TEXT[index];
        }
    }

    public class Trophies
    {
        private final UIStrings Strings = GetUIStrings("Trophies");

        public final String Bronze = Strings.TEXT[0];
        public final String Silver = Strings.TEXT[1];
        public final String Gold = Strings.TEXT[2];
        public final String BronzeDescription = Strings.TEXT[3];
        public final String SilverDescription = Strings.TEXT[4];
        public final String GoldDescription = Strings.TEXT[5];
        public final String BronzeLocked = Strings.TEXT[6];
        public final String SilverLocked = Strings.TEXT[7];
        public final String GoldLocked = Strings.TEXT[8];
        public final String GoldAccelWorld = Strings.TEXT[9];
        public final String PlatinumHint = Strings.TEXT[10];
        public final String PlatinumDescription = Strings.TEXT[11];
        public final String Platinum = Strings.TEXT[12];

        public final String BronzeFormatted(int ascension)
        {
            return PCLJUtils.Format(BronzeDescription, ascension);
        }
    }

    public class Tutorial
    {
        private final UIStrings Strings = GetUIStrings("Tutorial");

        public final String AffinityInfo = Strings.TEXT[0];
        public final String AffinityTutorial1 = Strings.TEXT[1];
    }

    public class TheUnnamedReign
    {
        public final UIStrings Strings = GetUIStrings("TheUnnamedReign");
        public final String Name = Strings.TEXT[0];
    }

    public class HandSelection
    {
        public final UIStrings Strings = GetUIStrings("HandSelection");
        public final String MoveToDrawPile = Strings.TEXT[0];
        public final String GenericBuff = Strings.TEXT[1];
        public final String Copy = Strings.TEXT[2];
        public final String Activate = Strings.TEXT[3];
        public final String Discard = DiscardAction.TEXT[0];
        public final String Exhaust = ExhaustAction.TEXT[0];
        public final String Obtain = CardRewardScreen.TEXT[1];
        public final String Retain = RetainCardsAction.TEXT[0];
    }

    public class GridSelection
    {
        public final UIStrings Strings = GetUIStrings("GridSelection");
        public final String DiscardUpTo_F1 = Strings.TEXT[0];
        public final String MoveToDrawPile_F1 = Strings.TEXT[1];
        public final String TransformInto_F1 = Strings.TEXT[2];
        public final String ChooseCards_F1 = Strings.TEXT[3];
        public final String Purge_F1 = Strings.TEXT[4];
        public final String Scry = Strings.TEXT[5];
        public final String Fetch_F1 = Strings.TEXT[6];
        public final String Pay_F1 = Strings.TEXT[7];
        public final String Give_F1 = Strings.TEXT[8];
        public final String Discard = DiscardAction.TEXT[0];
        public final String Exhaust = ExhaustAction.TEXT[0];
        public final String Cycle = GamblingChipAction.TEXT[1];
        public final String ChooseOneCard = CardRewardScreen.TEXT[1];

        public final String DiscardUpTo(int amount)
        {
            return PCLJUtils.Format(DiscardUpTo_F1, amount);
        }

        public final String MoveToDrawPile(int amount)
        {
            return PCLJUtils.Format(MoveToDrawPile_F1, amount);
        }

        public final String TransformInto(String name)
        {
            return PCLJUtils.Format(TransformInto_F1, name);
        }

        public final String ChooseCards(int amount)
        {
            return PCLJUtils.Format(ChooseCards_F1, amount);
        }

        public final String Purge(int amount)
        {
            return PCLJUtils.Format(Purge_F1, amount);
        }

        public final String Fetch(int amount)
        {
            return PCLJUtils.Format(Fetch_F1, amount);
        }

        public final String PayAffinity(int amount)
        {
            return PCLJUtils.Format(Pay_F1, amount);
        }

        public final String Give(int amount, Object item)
        {
            return PCLJUtils.Format(Give_F1, amount, item);
        }
    }

    private static UIStrings GetUIStrings(String id)
    {
        return GR.GetUIStrings(GR.CreateID(PCLResources.ID, id));
    }
}
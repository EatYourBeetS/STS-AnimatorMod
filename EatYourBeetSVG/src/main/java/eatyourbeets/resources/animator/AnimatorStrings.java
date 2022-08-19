package eatyourbeets.resources.animator;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class AnimatorStrings
{
    public Rewards Rewards;
    public Misc Misc;
    public Series Series;
    public CharacterSelect CharSelect;
    public SeriesSelection SeriesSelection;
    public SeriesSelectionButtons SeriesSelectionButtons;
    public SpecialActions SpecialActions;
    public Actions Actions;
    public Trophies Trophies;
    public Affinities Affinities;
    public Tutorial Tutorial;

    public void Initialize()
    {
        Misc = new Misc();
        Rewards = new Rewards();
        Series = new Series();
        CharSelect = new CharacterSelect();
        Actions = new Actions();
        Trophies = new Trophies();
        Affinities = new Affinities();
        Tutorial = new Tutorial();
        SeriesSelection = new SeriesSelection();
        SeriesSelectionButtons = new SeriesSelectionButtons();
        SpecialActions = new SpecialActions();
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

        public final String MaxHPBonus(int amount)
        {
            return JUtils.Format(MaxHPBonus_F1, amount);
        }

        public final String GoldBonus(int amount)
        {
            return JUtils.Format(GoldBonus_F1, amount);
        }
    }

    public class Series
    {
        private final UIStrings Strings = GetUIStrings("Series");

        public final String Series = Strings.EXTRA_TEXT[0];

        public final String SeriesName(int seriesID)
        {
            return Strings.TEXT.length > seriesID ? Strings.TEXT[seriesID] : null;
        }
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
        public final String PressControlToCycle = Strings.TEXT[7];
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
        public final String MaxCopiesHeader = Strings.TEXT[19];
        public final String MaxCopiesDescription = Strings.TEXT[20];
        public final String MulliganHeader = Strings.TEXT[21];
        public final String MulliganDescription = Strings.TEXT[22];
        public final String CannotSeal = Strings.TEXT[23];

        public final String MaxStacks(int stacks)
        {
            return JUtils.Format(MaxStacks_F1, stacks);
        }

        public final String GainBlockAboveMaxStacks(int stacks)
        {
            return JUtils.Format(GainBlockAboveMaxStacks_F1, stacks);
        }

        public final String MaxBlock(int block)
        {
            return JUtils.Format(MaxBlock_F1, block);
        }
    }

    public class CharacterSelect
    {
        private final UIStrings Strings = GetUIStrings("CharacterSelect");

        public final String LeftText = Strings.TEXT[0];  // Starting Cards:
        public final String RightText = Strings.TEXT[1]; // ##############
        public final String InvalidLoadout = Strings.TEXT[3];
        public final String DeckEditor = Strings.TEXT[5];
        public final String DeckEditorInfo = Strings.TEXT[6];

        public final String UnlocksAtLevel(int unlockLevel, int currentLevel)
        {
            return JUtils.Format(Strings.TEXT[2], unlockLevel, currentLevel);
        }

        public final String UnlocksAtAscension(int ascension)
        {
            return JUtils.Format(Strings.TEXT[4], ascension);
        }

        public final String ObtainBronzeAtAscension(int ascension)
        {
            return JUtils.Format(Strings.TEXT[7], ascension);
        }
    }

    public class SeriesSelection
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelection");

        public final String CardPoolSizeRequirement_F2 = Strings.TEXT[3];
        public final String PickupBonusHeader = Strings.TEXT[4];
        public final String PickupBonusBody = Strings.TEXT[5];
        public final String RightClickToPreview = Strings.TEXT[6];
        public final String PlotArmorInfo = Strings.TEXT[7];

        public final String CardPoolSizeRequirement(int minimumCards, int cardsForBonusRelic)
        {
            return JUtils.Format(CardPoolSizeRequirement_F2, minimumCards, cardsForBonusRelic);
        }

        public final String ContainsNCards(int cardCount)
        {
            return cardCount > 0 ? JUtils.Format(Strings.TEXT[0], cardCount) : "";
        }

        public final String ContainsNCards_Promoted(int cardCount)
        {
            return ContainsNCards(cardCount) + " NL " + Strings.TEXT[1];
        }

        public final String ContainsNCards_Beta(int cardCount)
        {
            return ContainsNCards(cardCount) + " NL " + Strings.TEXT[2];
        }
    }

    public class SeriesSelectionButtons
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelectionButtons");

        public final String ShowBetaSeries = Strings.TEXT[0];
        public final String DeselectAll = Strings.TEXT[1];
        public final String SelectRandomCards_F1 = Strings.TEXT[2];
        public final String SelectRandomSeries_F1 = Strings.TEXT[3];
        public final String SelectAll = Strings.TEXT[4];
        public final String ShowCardPool = Strings.TEXT[5];
        public final String Proceed = Strings.TEXT[6];
        public final String PlotArmor = Strings.TEXT[7];

        public final String SelectRandomCards(int cards)
        {
            return JUtils.Format(SelectRandomCards_F1, cards);
        }

        public final String SelectRandomSeries(int series)
        {
            return JUtils.Format(SelectRandomSeries_F1, series);
        }
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

        public final String AinzIntangible(boolean addPeriod)
        {
            return Get(12) + (addPeriod ? LocalizedStrings.PERIOD : "");
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
            return Format(addPeriod, 22, null);
        }

        public final String PayEnergy(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 23, amount);
        }

        public final String LoseHP(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 24, amount);
        }

        public final String LoseMHP(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 25, amount);
        }

        public final String RecoverHP(int amount, boolean addPeriod)
        {
            return Format(addPeriod, 26, amount);
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

            return JUtils.Format(text, amount, extra) + (addPeriod ? LocalizedStrings.PERIOD : "");
        }

        public final String Get(int index)
        {
            return Strings.TEXT[index];
        }
    }

    public class SpecialActions
    {
        private final UIStrings Strings = GetUIStrings("SpecialActions");
        private final String[] TITLES = Strings.TEXT;
        private final String[] DESCRIPTIONS = Strings.EXTRA_TEXT;
        private int INDEX = 0;

        public final String T_Downgrade_F1 = Strings.TEXT[INDEX++];
        public final String D_Downgrade_F0 = Strings.TEXT[INDEX++];

        public final String Downgrade_T(int gold)
        {
            return JUtils.Format(T_Downgrade_F1, gold);
        }

        public final String Downgrade_D()
        {
            return D_Downgrade_F0;
        }

        public final String T_Improve_F1 = Strings.TEXT[INDEX++];
        public final String D_Improve_F0 = Strings.TEXT[INDEX++];

        public final String Improve_T(int gold)
        {
            return JUtils.Format(T_Improve_F1, gold);
        }

        public final String Improve_D()
        {
            return D_Improve_F0;
        }

        public final String T_Transform_GriefSeed_F0 = Strings.TEXT[INDEX++];
        public final String T_Transform_LoseHP_F1 = Strings.TEXT[INDEX++];
        public final String T_Transform_LoseMaxHP_F1 = Strings.TEXT[INDEX++];
        public final String D_Transform_Generic_F1 = Strings.TEXT[INDEX++];
        public final String D_Transform_RequireCard_F2 = Strings.TEXT[INDEX++];
        public final String D_Transform_RemoveCards_F3 = Strings.TEXT[INDEX++];

        public final String TransformAndObtainGriefSeed_T()
        {
            return T_Transform_GriefSeed_F0;
        }

        public final String TransformAndLoseHP_T(int amount)
        {
            return JUtils.Format(T_Transform_LoseHP_F1, amount);
        }

        public final String TransformAndLoseMaxHP_T(int amount)
        {
            return JUtils.Format(T_Transform_LoseMaxHP_F1, amount);
        }

        public final String TransformGeneric_D(String cardName)
        {
            return JUtils.Format(D_Transform_Generic_F1, JUtils.ModifyString(cardName, w -> "#y" + w));
        }

        public final String RequireCardAndTransform_D(String requiredCard, String toObtain)
        {
            final String f1 = JUtils.ModifyString(requiredCard, w -> "#y" + w);
            final String f2 = JUtils.ModifyString(toObtain, w -> "#y" + w);
            return JUtils.Format(D_Transform_RequireCard_F2, f1, f2);
        }

        public final String RemoveAndTransform_D(String toRemove1, String toRemove2, String toObtain)
        {
            final String f1 = JUtils.ModifyString(toRemove1, w -> "#y" + w);
            final String f2 = JUtils.ModifyString(toRemove2, w -> "#y" + w);
            final String f3 = JUtils.ModifyString(toObtain, w -> "#y" + w);
            return JUtils.Format(D_Transform_RemoveCards_F3, f1, f2, f3);
        }

        public final String T_Obtain_GainVulnerable_F1 = Strings.TEXT[INDEX++];
        public final String D_Obtain_GainVulnerable_F2 = Strings.TEXT[INDEX++];

        public final String ObtainCardAndGainVulnerable(int vulnerable)
        {
            return JUtils.Format(T_Obtain_GainVulnerable_F1, vulnerable);
        }

        public final String ObtainCardAndGainVulnerable_D(String cardName, int vulnerable)
        {
            return JUtils.Format(D_Obtain_GainVulnerable_F2, JUtils.ModifyString(cardName, w -> "#y" + w), vulnerable);
        }

        public final String T_ObtainCurse_Heal_F1 = Strings.TEXT[INDEX++];
        public final String D_ObtainCurse_RequireAmount_F2 = Strings.TEXT[INDEX++];

        public final String ObtainCurseAndHeal_T(int heal)
        {
            return JUtils.Format(T_ObtainCurse_Heal_F1, heal);
        }

        public final String ObtainCurseAndHeal_D(String cardName, int requiredCurses)
        {
            return JUtils.Format(D_ObtainCurse_RequireAmount_F2, requiredCurses, JUtils.ModifyString(cardName, w -> "#y" + w));
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
        public final String SilverKancolle = Strings.TEXT[6];
        public final String GoldKancolle = Strings.TEXT[7];
        public final String SilverAccelWorld = Strings.TEXT[8];
        public final String GoldAccelWorld = Strings.TEXT[9];
        public final String PlatinumHint = Strings.TEXT[10];
        public final String PlatinumDescription = Strings.TEXT[11];
        public final String Platinum = Strings.TEXT[12];
    }

    public class Affinities
    {
        private final UIStrings Strings = GetUIStrings("Affinities");
        private final String AffinityStatus_F2 = Strings.TEXT[0];
        private final String SealedUses_F1 = Strings.TEXT[1];
        private final String LockMessage = Strings.TEXT[2];
        private final String Hold = Strings.TEXT[3];
        private final String Toggle = Strings.TEXT[4];

        public String AffinityStatus(int current, int max)
        {
            return JUtils.Format(AffinityStatus_F2, current, max);
        }

        public String SealedUses(int availableUses)
        {
            return JUtils.Format(SealedUses_F1, availableUses);
        }

        public String LockMessage(boolean hold)
        {
            return LockMessage + (hold ? Hold : Toggle);
        }
    }

    public class Tutorial
    {
        private final UIStrings Strings = GetUIStrings("Tutorial");

        public final String ClickToCycle_F2 = Strings.TEXT[0];

        public String ClickToCycle(int current, int max)
        {
            return JUtils.Format(ClickToCycle_F2, current, max);
        }

        public String[] GetStrings()
        {
            final String[] result = new String[5];
            System.arraycopy(Strings.TEXT, 1, result, 0, 5);
            return result;
        }
    }

    private static UIStrings GetUIStrings(String id)
    {
        return GR.GetUIStrings(GR.CreateID(AnimatorResources.ID, id));
    }
}
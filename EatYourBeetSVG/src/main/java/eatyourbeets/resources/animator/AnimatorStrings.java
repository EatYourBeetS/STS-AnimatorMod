package eatyourbeets.resources.animator;

import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class AnimatorStrings
{
    public Tips Tips;
    public Rewards Rewards;
    public Misc Misc;
    public Synergies Synergies;
    public CharacterSelect CharSelect;
    public SeriesSelection SeriesSelection;
    public SeriesSelectionButtons SeriesSelectionButtons;
    public Actions Actions;
    public Trophies Trophies;

    public void Initialize()
    {
        Misc = new Misc();
        Tips = new Tips();
        Rewards = new Rewards();
        Synergies = new Synergies();
        CharSelect = new CharacterSelect();
        Actions = new Actions();
        Trophies = new Trophies();
        SeriesSelection = new SeriesSelection();
        SeriesSelectionButtons = new SeriesSelectionButtons();
    }

    public class Tips
    {
        private final UIStrings Strings = GetUIStrings("Tips");

        public final String Content(int id)
        {
            return Strings.TEXT[(id * 2) + 1];
        }

        public final String Header(int id)
        {
            return Strings.TEXT[(id * 2)];
        }
    }

    public class Rewards
    {
        private final UIStrings Strings = GetUIStrings("Rewards");

        public final String Description = Strings.TEXT[0];
        public final String BonusRelic = Strings.TEXT[1];
        public final String CursedRelic = Strings.TEXT[2];
        public final String Banish = Strings.TEXT[3];
        public final String AuraEffects = Strings.TEXT[6];

        public final String MaxHPBonus(int amount)
        {
            return JUtils.Format(Strings.TEXT[4], amount);
        }

        public final String GoldBonus(int amount)
        {
            return JUtils.Format(Strings.TEXT[5], amount);
        }
    }

    public class Synergies
    {
        private final UIStrings Strings = GetUIStrings("Synergies");

        public final String Series = Strings.EXTRA_TEXT[0];

        public final String SynergyName(int synergyID)
        {
            return Strings.TEXT[synergyID];
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

        public final String SynergyName(int synergyID)
        {
            return Strings.TEXT[synergyID];
        }
    }

    public class CharacterSelect
    {
        private final UIStrings Strings = GetUIStrings("CharacterSelect");

        public final String LeftText = Strings.TEXT[0];  // Starting Cards:
        public final String RightText = Strings.TEXT[1]; // ##############

        public final String UnlocksAtLevel(int unlockLevel, int currentLevel)
        {
            return JUtils.Format(Strings.TEXT[2], unlockLevel, currentLevel);
        }
    }

    public class SeriesSelection
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelection");

        public final String PurgingStoneRequirement = Strings.TEXT[3];
        public final String PickupBonusHeader = Strings.TEXT[4];
        public final String PickupBonusBody = Strings.TEXT[5];
        public final String RightClickToPreview = Strings.TEXT[6];

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
        public final String SelectAll = Strings.TEXT[3];
        public final String ShowCardPool = Strings.TEXT[4];
        public final String Proceed = Strings.TEXT[5];

        public final String SelectRandom(int cards)
        {
            return JUtils.Format(Strings.TEXT[2], cards) ;
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

    private static UIStrings GetUIStrings(String id)
    {
        return GR.GetUIStrings(GR.CreateID(AnimatorResources.ID, id));
    }
}
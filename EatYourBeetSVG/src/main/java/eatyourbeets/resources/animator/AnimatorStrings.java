package eatyourbeets.resources.animator;

import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

public class AnimatorStrings
{
    public Tips Tips;
    public Rewards Rewards;
    public Synergies Synergies;
    public CharacterSelect CharSelect;
    public SeriesSelection SeriesSelection;
    public Special Special;
    public Trophies Trophies;

    public void Initialize()
    {
        Tips = new Tips();
        Rewards = new Rewards();
        Synergies = new Synergies();
        CharSelect = new CharacterSelect();
        Special = new Special();
        Trophies = new Trophies();
        SeriesSelection = new SeriesSelection();
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

    public class CharacterSelect
    {
        private final UIStrings Strings = GetUIStrings("CharacterSelect");

        public final String LeftText = Strings.TEXT[0];  // Starting Cards:
        public final String RightText = Strings.TEXT[1]; // ##############

        public final String UnlocksAtLevel(int unlockLevel, int currentLevel)
        {
            return JavaUtilities.Format(Strings.TEXT[2], unlockLevel, currentLevel);
        }
    }

    public class SeriesSelection
    {
        private final UIStrings Strings = GetUIStrings("SeriesSelection");

        public final String PurgingStoneRequirement = Strings.TEXT[3];

        public final String ContainsNCards(int cardCount)
        {
            return JavaUtilities.Format(Strings.TEXT[0], cardCount);
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

    public class Special
    {
        private final UIStrings Strings = GetUIStrings("SpecialEffects");

        public final String Get(int index)
        {
            // TODO: A field for each effect
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
package eatyourbeets.characters;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.AnimatorResources;
import eatyourbeets.Utilities;
import eatyourbeets.cards.animator.Defend;
import eatyourbeets.cards.animator.Strike;
import eatyourbeets.characters.Loadouts.*;
import eatyourbeets.powers.PlayerStatistics;
import patches.AbstractEnums;

import java.util.ArrayList;
import java.util.StringJoiner;

public class AnimatorCharacterSelect
{
    private static int index = 0;
    private static final ArrayList<AnimatorCustomLoadout> customLoadouts = new ArrayList<>();

    protected static final String[] uiText = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.CharacterSelect).TEXT;

    public static AnimatorCustomLoadout GetSelectedLoadout(boolean startingGame)
    {
        AnimatorCustomLoadout loadout = customLoadouts.get(index);
        if (startingGame && loadout instanceof Random)
        {
            ArrayList<AnimatorCustomLoadout> unlocked = new ArrayList<>();
            int currentLevel = UnlockTracker.getUnlockLevel(AbstractEnums.Characters.THE_ANIMATOR);
            for (AnimatorCustomLoadout item : customLoadouts)
            {
                if (!(item instanceof Random) && currentLevel >= item.unlockLevel)
                {
                    unlocked.add(item);
                }
            }

            AnimatorCustomLoadout newLoadout = Utilities.GetRandomElement(unlocked, new com.megacrit.cardcrawl.random.Random());
            if (newLoadout == null)
            {
                index = 0;
                newLoadout = customLoadouts.get(0);
            }
            else
            {
                index = customLoadouts.indexOf(newLoadout);
            }

            return newLoadout;
        }
        else
        {
            return loadout;
        }
    }

    public static void NextLoadout()
    {
        index += 1;
        if (index >= customLoadouts.size())
        {
            index = 0;
        }
    }

    public static void PreviousLoadout()
    {
        index -= 1;
        if (index < 0)
        {
            index = customLoadouts.size() - 1;
        }
    }

    public static void OnTrueVictory(int ascensionLevel)
    {
        if (PlayerStatistics.SaveData.EnteredUnnamedReign)
        {
            if (AnimatorCustomLoadout.specialTrophies.trophy1 < 0)
            {
                AnimatorCustomLoadout.specialTrophies.trophy1 = 0;
            }

            if (AbstractDungeon.isAscensionMode)
            {
                AnimatorCustomLoadout.specialTrophies.trophy1 += 1 + AbstractDungeon.ascensionLevel / 5;
            }
            else
            {
                AnimatorCustomLoadout.specialTrophies.trophy1 += 1;
            }
        }

        for (AnimatorCustomLoadout loadout : customLoadouts)
        {
            if (!(loadout instanceof Random))
            {
                loadout.OnTrueVictory(GetSelectedLoadout(false), ascensionLevel);
            }
        }

        AnimatorMetrics.SaveTrophies(true);
    }

    private static void AddLoadout(AnimatorCustomLoadout loadout, int level, String description)
    {
        StringJoiner sj = new StringJoiner(", ");
        for (String s : loadout.GetStartingDeck())
        {
            if (!s.contains(Strike.ID) && !s.contains(Defend.ID))
            {
                sj.add(CardLibrary.getCardNameFromKey(s));
            }
        }
        loadout.description = sj.toString();
        //loadout.description = description;
        loadout.unlockLevel = level;
        customLoadouts.add(loadout);
    }

    static
    {
//        String recommended = uiText[5];
//        String balanced = uiText[6];
//        String unbalanced = uiText[7];
//        String veryUnbalanced = uiText[8];
//        String special = uiText[9];

        AddLoadout(new Konosuba()           , 0, "");
        AddLoadout(new Gate()               , 1, "");
        AddLoadout(new Elsword()            , 1, "");
        AddLoadout(new NoGameNoLife()       , 1, "");
        AddLoadout(new OwariNoSeraph()      , 2, "");
        AddLoadout(new GoblinSlayer()       , 2, "");
        AddLoadout(new Katanagatari()       , 2, "");
        AddLoadout(new FullmetalAlchemist() , 2, "");
        AddLoadout(new Fate()               , 3, "");
        AddLoadout(new Overlord()           , 3, "");
        AddLoadout(new Chaika()             , 3, "");
        AddLoadout(new TenSura()            , 3, "");
        AddLoadout(new OnePunchMan()        , 4, "");
        AddLoadout(new Kancolle()           , 4, "");
        AddLoadout(new AccelWorld()         , 4, "");
        AddLoadout(new Random()             , 0, "");

        int synergyID = AnimatorMetrics.lastLoadout;

        Utilities.Logger.info("Last Layout: " + synergyID);

        //noinspection ConstantConditions
        for (int i = 0; i < customLoadouts.size(); i++)
        {
            Utilities.Logger.info(customLoadouts.get(i).Name + " (" + customLoadouts.get(i).ID + ")");
            if (synergyID == customLoadouts.get(i).ID)
            {
                index = i;
                break;
            }
        }
    }
}
package eatyourbeets.characters;

import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.animator.Defend;
import eatyourbeets.cards.animator.Strike;
import eatyourbeets.characters.Loadouts.*;

import java.util.ArrayList;
import java.util.StringJoiner;

public class AnimatorCharacterSelect
{
    private static int index;
    private static final ArrayList<AnimatorCustomLoadout> customLoadouts;

    protected static final String[] uiText = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.CharacterSelect).TEXT;

    public static AnimatorCustomLoadout GetSelectedLoadout()
    {
        return customLoadouts.get(index);
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

    private static void AddLoadout(AnimatorCustomLoadout loadout, int level, String description)
    {
        StringJoiner sj = new StringJoiner(", ");
        for (String s : loadout.GetStartingDeck())
        {
            if (!s.equals(Strike.ID) && !s.equals(Defend.ID))
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
        index = 0;
        customLoadouts = new ArrayList<>();

        String recommended = uiText[5];
        String balanced = uiText[6];
        String unbalanced = uiText[7];
        String veryUnbalanced = uiText[8];
        String special = uiText[9];

        AddLoadout(new Konosuba()           , 0, recommended);
        AddLoadout(new Gate()               , 1, balanced);
        AddLoadout(new Elsword()            , 1, balanced);
        AddLoadout(new NoGameNoLife()       , 1, balanced);
        AddLoadout(new OwariNoSeraph()      , 2, unbalanced);
        AddLoadout(new GoblinSlayer()       , 2, unbalanced);
        AddLoadout(new Katanagatari()       , 2, unbalanced);
        AddLoadout(new FullmetalAlchemist() , 2, unbalanced);
        AddLoadout(new Fate()               , 3, veryUnbalanced);
        AddLoadout(new Overlord()           , 3, veryUnbalanced);
        AddLoadout(new Chaika()             , 3, veryUnbalanced);
        AddLoadout(new Kancolle()           , 4, special);
    }
}
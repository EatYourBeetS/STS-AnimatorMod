package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;

import java.util.ArrayList;

public class _FakeLoadout extends AnimatorLoadout
{
    public _FakeLoadout()
    {
        super("<Error>");
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(QuestionMark.DATA, 10);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return null;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return null;
    }

    @Override
    public void OnVictory(AnimatorLoadout currentLoadout, int ascensionLevel)
    {
        //
    }

    @Override
    public AnimatorTrophies GetTrophies()
    {
        return null;
    }
}

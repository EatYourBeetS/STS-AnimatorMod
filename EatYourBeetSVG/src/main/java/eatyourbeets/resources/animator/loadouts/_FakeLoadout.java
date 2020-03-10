package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.ui.animator.characterSelection.AnimatorLoadoutRenderer;

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
    public EYBCardData GetSymbolicCard()
    {
        return null;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return null;
    }

    public void OnVictory(AnimatorLoadoutRenderer currentLoadout, int ascensionLevel)
    {
        //
    }

    @Override
    public AnimatorTrophies GetTrophies()
    {
        return null;
    }
}

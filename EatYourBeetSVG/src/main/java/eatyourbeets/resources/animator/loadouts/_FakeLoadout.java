package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

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
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return null;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        return null;
    }
}

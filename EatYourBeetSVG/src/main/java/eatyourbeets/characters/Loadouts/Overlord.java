package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.Cocytus;
import eatyourbeets.cards.animator.PandorasActor;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Overlord extends AnimatorCustomLoadout
{
    public Overlord()
    {
        Synergy s = Synergies.Overlord;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = super.GetStartingDeck();
        res.add(Cocytus.ID);
        res.add(PandorasActor.ID);

        return res;
    }
}

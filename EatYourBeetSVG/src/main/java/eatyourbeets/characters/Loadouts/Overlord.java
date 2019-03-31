package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.*;
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
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_Overlord.ID);
        res.add(Defend_Overlord.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Cocytus.ID);
        res.add(PandorasActor.ID);

        return res;
    }
}

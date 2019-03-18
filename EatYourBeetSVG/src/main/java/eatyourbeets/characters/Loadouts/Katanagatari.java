package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.Azekura;
import eatyourbeets.cards.animator.Emonzaemon;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Katanagatari extends AnimatorCustomLoadout
{
    public Katanagatari()
    {
        Synergy s = Synergies.Katanagatari;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = super.GetStartingDeck();
        res.add(Azekura.ID);
        res.add(Emonzaemon.ID);

        return res;
    }
}

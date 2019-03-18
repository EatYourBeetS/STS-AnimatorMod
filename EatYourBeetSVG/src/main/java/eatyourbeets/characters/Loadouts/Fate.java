package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.Alexander;
import eatyourbeets.cards.animator.RinTohsaka;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Fate extends AnimatorCustomLoadout
{
    public Fate()
    {
        Synergy s = Synergies.Fate;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = super.GetStartingDeck();
        res.add(RinTohsaka.ID);
        res.add(Alexander.ID);

        return res;
    }
}

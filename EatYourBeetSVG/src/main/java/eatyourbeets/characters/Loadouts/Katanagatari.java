package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.*;
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
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_Katanagatari.ID);
        res.add(Defend_Katanagatari.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Azekura.ID);
        res.add(Emonzaemon.ID);

        return res;
    }
}

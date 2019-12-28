package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Katanagatari;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Katanagatari;
import eatyourbeets.cards.animator.series.Katanagatari.Azekura;
import eatyourbeets.cards.animator.series.Katanagatari.Emonzaemon;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Katanagatari extends AnimatorCustomLoadout
{
    public Katanagatari()
    {
        Synergy s = Synergies.Katanagatari;

        this.ID = s.ID;
        this.Name = s.Name;
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

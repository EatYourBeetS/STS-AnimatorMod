package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Konosuba;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Konosuba;
import eatyourbeets.cards.animator.series.Konosuba.Aqua;
import eatyourbeets.cards.animator.series.Konosuba.Kazuma;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Konosuba extends AnimatorCustomLoadout
{
    public Konosuba()
    {
        Synergy s = Synergies.Konosuba;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_Konosuba.ID);
        res.add(Defend_Konosuba.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Kazuma.ID);
        res.add(Aqua.ID);

        return res;
    }
}

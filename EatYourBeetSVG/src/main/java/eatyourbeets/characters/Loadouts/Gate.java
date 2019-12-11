package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_GATE;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_GATE;
import eatyourbeets.cards.animator.series.GATE.Kuribayashi;
import eatyourbeets.cards.animator.series.GATE.TukaLunaMarceau;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Gate extends AnimatorCustomLoadout
{
    public Gate()
    {
        Synergy s = Synergies.Gate;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_GATE.ID);
        res.add(Defend_GATE.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(TukaLunaMarceau.ID);
        res.add(Kuribayashi.ID);

        return res;
    }
}

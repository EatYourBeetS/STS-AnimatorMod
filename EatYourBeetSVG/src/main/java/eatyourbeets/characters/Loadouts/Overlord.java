package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Overlord;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Overlord;
import eatyourbeets.cards.animator.series.Overlord.Cocytus;
import eatyourbeets.cards.animator.series.Overlord.Demiurge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
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
        res.add(Demiurge.ID);

        return res;
    }
}

package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_OnePunchMan;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_OnePunchMan;
import eatyourbeets.cards.animator.series.OnePunchMan.Genos;
import eatyourbeets.cards.animator.series.OnePunchMan.MumenRider;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class OnePunchMan extends AnimatorCustomLoadout
{
    public OnePunchMan()
    {
        Synergy s = Synergies.OnePunchMan;

        this.ID = s.ID;
        this.Name = s.Name;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_OnePunchMan.ID);
        res.add(Defend_OnePunchMan.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Genos.ID);
        res.add(MumenRider.ID);

        return res;
    }
}

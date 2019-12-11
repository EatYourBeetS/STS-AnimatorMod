package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Fate;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Fate;
import eatyourbeets.cards.animator.series.Fate.Alexander;
import eatyourbeets.cards.animator.series.Fate.Caster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
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
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_Fate.ID);
        res.add(Defend_Fate.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Caster.ID);
        res.add(Alexander.ID);

        return res;
    }
}

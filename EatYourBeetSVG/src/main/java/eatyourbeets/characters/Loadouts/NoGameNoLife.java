package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_NoGameNoLife;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_NoGameNoLife;
import eatyourbeets.cards.animator.series.NoGameNoLife.DolaCouronne;
import eatyourbeets.cards.animator.series.NoGameNoLife.DolaSchwi;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class NoGameNoLife extends AnimatorCustomLoadout
{
    public NoGameNoLife()
    {
        Synergy s = Synergies.NoGameNoLife;

        this.ID = s.ID;
        this.Name = s.Name;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_NoGameNoLife.ID);
        res.add(Defend_NoGameNoLife.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(DolaSchwi.ID);
        res.add(DolaCouronne.ID);

        return res;
    }
}

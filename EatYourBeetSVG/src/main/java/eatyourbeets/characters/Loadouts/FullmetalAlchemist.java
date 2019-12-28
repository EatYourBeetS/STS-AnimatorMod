package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_FullmetalAlchemist;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_FullmetalAlchemist;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricAlphonse;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.ElricEdward;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class FullmetalAlchemist extends AnimatorCustomLoadout
{
    public FullmetalAlchemist()
    {
        Synergy s = Synergies.FullmetalAlchemist;

        this.ID = s.ID;
        this.Name = s.Name;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_FullmetalAlchemist.ID);
        res.add(Defend_FullmetalAlchemist.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(ElricEdward.ID);
        res.add(ElricAlphonse.ID);

        return res;
    }
}

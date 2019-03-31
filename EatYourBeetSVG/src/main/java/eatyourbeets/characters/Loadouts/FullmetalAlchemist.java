package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.*;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class FullmetalAlchemist extends AnimatorCustomLoadout
{
    public FullmetalAlchemist()
    {
        Synergy s = Synergies.FullmetalAlchemist;

        this.ID = s.ID;
        this.Name = s.NAME;
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

package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.Cocytus;
import eatyourbeets.cards.animator.ElricAlphonse;
import eatyourbeets.cards.animator.ElricEdward;
import eatyourbeets.cards.animator.NarberalGamma;
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
        ArrayList<String> res = super.GetStartingDeck();
        res.add(ElricEdward.ID);
        res.add(ElricAlphonse.ID);

        return res;
    }
}

package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.DwarfShaman;
import eatyourbeets.cards.animator.Priestess;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class GoblinSlayer extends AnimatorCustomLoadout
{
    public GoblinSlayer()
    {
        Synergy s = Synergies.GoblinSlayer;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = super.GetStartingDeck();
        res.add(Priestess.ID);
        res.add(DwarfShaman.ID);

        return res;
    }
}

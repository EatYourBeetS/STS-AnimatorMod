package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.*;
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
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_GoblinSlayer.ID);
        res.add(Defend_GoblinSlayer.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(LizardPriest.ID);
        res.add(DwarfShaman.ID);

        return res;
    }
}

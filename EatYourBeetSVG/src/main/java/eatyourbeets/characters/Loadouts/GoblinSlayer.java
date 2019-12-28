package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_GoblinSlayer;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_GoblinSlayer;
import eatyourbeets.cards.animator.series.GoblinSlayer.DwarfShaman;
import eatyourbeets.cards.animator.series.GoblinSlayer.LizardPriest;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class GoblinSlayer extends AnimatorCustomLoadout
{
    public GoblinSlayer()
    {
        Synergy s = Synergies.GoblinSlayer;

        this.ID = s.ID;
        this.Name = s.Name;
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

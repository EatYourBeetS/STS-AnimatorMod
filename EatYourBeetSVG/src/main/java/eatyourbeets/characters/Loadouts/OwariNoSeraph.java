package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_OwariNoSeraph;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_OwariNoSeraph;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Mikaela;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Shinoa;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class OwariNoSeraph extends AnimatorCustomLoadout
{
    public OwariNoSeraph()
    {
        Synergy s = Synergies.OwariNoSeraph;

        this.ID = s.ID;
        this.Name = s.Name;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_OwariNoSeraph.ID);
        res.add(Defend_OwariNoSeraph.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Shinoa.ID);
        res.add(Mikaela.ID);

        return res;
    }
}

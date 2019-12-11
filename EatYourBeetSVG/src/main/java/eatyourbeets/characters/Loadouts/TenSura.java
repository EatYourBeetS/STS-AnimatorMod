package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_TenSura;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_TenSura;
import eatyourbeets.cards.animator.series.TenseiSlime.Benimaru;
import eatyourbeets.cards.animator.series.TenseiSlime.Shuna;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class TenSura extends AnimatorCustomLoadout
{
    public TenSura()
    {
        Synergy s = Synergies.TenSura;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_TenSura.ID);
        res.add(Defend_TenSura.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Benimaru.ID);
        res.add(Shuna.ID);

        return res;
    }
}

package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_HitsugiNoChaika;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_HitsugiNoChaika;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.AcuraAkari;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.Gillette;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Chaika extends AnimatorCustomLoadout
{
    public Chaika()
    {
        Synergy s = Synergies.Chaika;

        this.ID = s.ID;
        this.Name = s.Name;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_HitsugiNoChaika.ID);
        res.add(Defend_HitsugiNoChaika.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(AcuraAkari.ID);
        res.add(Gillette.ID);

        return res;
    }
}

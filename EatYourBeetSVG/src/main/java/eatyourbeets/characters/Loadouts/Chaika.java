package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.*;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Chaika extends AnimatorCustomLoadout
{
    public Chaika()
    {
        Synergy s = Synergies.Chaika;

        this.ID = s.ID;
        this.Name = s.NAME;
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
        res.add(AcuraTooru.ID);

        return res;
    }
}

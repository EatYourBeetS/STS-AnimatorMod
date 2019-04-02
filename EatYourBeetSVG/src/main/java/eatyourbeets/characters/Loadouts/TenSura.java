package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.*;
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
        //res.add(Millim.ID);
        //res.add(Hakurou.ID);
        //res.add(Kaijin.ID);
        //res.add(Souei.ID);
        res.add(Shuna.ID);
        //res.add(GazelDwargon.ID);
        //res.add(Vesta.ID);
        //res.add(Shizu.ID);
        //res.add(Rimuru.ID);

        return res;
    }
}

package eatyourbeets.characters.Loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Elsword;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Elsword;
import eatyourbeets.cards.animator.series.Elsword.Chung;
import eatyourbeets.cards.animator.series.Elsword.Raven;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;

import java.util.ArrayList;

public class Elsword extends AnimatorCustomLoadout
{
    public Elsword()
    {
        Synergy s = Synergies.Elsword;

        this.ID = s.ID;
        this.Name = s.Name;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_Elsword.ID);
        res.add(Defend_Elsword.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Chung.ID);
        res.add(Raven.ID);

        return res;
    }
}

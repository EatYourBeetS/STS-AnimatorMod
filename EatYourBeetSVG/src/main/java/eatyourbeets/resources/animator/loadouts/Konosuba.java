package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Konosuba;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Konosuba;
import eatyourbeets.cards.animator.series.Konosuba.Aqua;
import eatyourbeets.cards.animator.series.Konosuba.Kazuma;
import eatyourbeets.cards.animator.series.Konosuba.Megumin;
import eatyourbeets.cards.animator.ultrarare.Chomusuke;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

import java.util.ArrayList;

public class Konosuba extends AnimatorLoadout
{
    public Konosuba()
    {
        super(Synergies.Konosuba);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_Konosuba.ID);
            startingDeck.add(Defend_Konosuba.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Kazuma.ID);
            startingDeck.add(Aqua.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return Megumin.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new Chomusuke();
        }

        return ultraRare;
    }
}

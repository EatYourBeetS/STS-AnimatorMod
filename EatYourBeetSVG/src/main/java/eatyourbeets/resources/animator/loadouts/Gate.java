package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_GATE;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_GATE;
import eatyourbeets.cards.animator.series.GATE.Kuribayashi;
import eatyourbeets.cards.animator.series.GATE.RoryMercury;
import eatyourbeets.cards.animator.series.GATE.TukaLunaMarceau;
import eatyourbeets.cards.animator.ultrarare.Giselle;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class Gate extends AnimatorLoadout
{
    public Gate()
    {
        super(Synergies.Gate);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_GATE.ID);
            startingDeck.add(Defend_GATE.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(TukaLunaMarceau.DATA.ID);
            startingDeck.add(Kuribayashi.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return RoryMercury.DATA.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new Giselle();
        }

        return ultraRare;
    }
}

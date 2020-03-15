package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.beta.DateALive.*;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class DateALive extends AnimatorLoadout {

    public DateALive()
    {
        super(Synergies.DateALive);

        IsBeta = true;
    }

    @Override
    public ArrayList<String> GetStartingDeck() {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_DateALive.ID);
            startingDeck.add(Defend_DateALive.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(ShidoItsuka.DATA.ID);
            startingDeck.add(ReineMurasame.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard() {
        return TohkaYatogami.DATA;
    }

    @Override
    public EYBCardData GetUltraRare() {
        //Mio Takamiya
        return null;
    }
}

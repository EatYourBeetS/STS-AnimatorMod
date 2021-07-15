package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.beta.basic.Defend_DateALive;
import eatyourbeets.cards.animator.beta.basic.Strike_DateALive;
import eatyourbeets.cards.animator.beta.series.DateALive.ShidoItsuka;
import eatyourbeets.cards.animator.beta.series.DateALive.TohkaYatogami;
import eatyourbeets.cards.animator.beta.series.DateALive.YamaiSisters;
import eatyourbeets.cards.animator.beta.ultrarare.MioTakamiya;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class DateALive extends AnimatorLoadout {

    public DateALive()
    {
        super(CardSeries.DateALive);

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
            startingDeck.add(YamaiSisters.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard() {
        return TohkaYatogami.DATA;
    }

    @Override
    public EYBCardData GetUltraRare() {
        return MioTakamiya.DATA;
    }
}

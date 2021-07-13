package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Fate;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Fate;
import eatyourbeets.cards.animator.series.Fate.Alexander;
import eatyourbeets.cards.animator.series.Fate.Rider;
import eatyourbeets.cards.animator.series.Fate.Saber;
import eatyourbeets.cards.animator.ultrarare.JeanneDArc;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class Fate extends AnimatorLoadout
{
    public Fate()
    {
        super(CardSeries.Fate);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_Fate.ID);
            startingDeck.add(Defend_Fate.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Alexander.DATA.ID);
            startingDeck.add(Rider.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Saber.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return JeanneDArc.DATA;
    }
}

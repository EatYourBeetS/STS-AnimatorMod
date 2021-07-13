package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_HitsugiNoChaika;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_HitsugiNoChaika;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.AcuraAkari;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.ChaikaTrabant;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.Gillette;
import eatyourbeets.cards.animator.ultrarare.NivaLada;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class Chaika extends AnimatorLoadout
{
    public Chaika()
    {
        super(CardSeries.HitsugiNoChaika);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_HitsugiNoChaika.ID);
            startingDeck.add(Defend_HitsugiNoChaika.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(AcuraAkari.DATA.ID);
            startingDeck.add(Gillette.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return ChaikaTrabant.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return NivaLada.DATA;
    }
}

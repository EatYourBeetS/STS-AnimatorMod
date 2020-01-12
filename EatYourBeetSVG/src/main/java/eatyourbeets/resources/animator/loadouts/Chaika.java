package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_HitsugiNoChaika;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_HitsugiNoChaika;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.AcuraAkari;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.ChaikaTrabant;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.Gillette;
import eatyourbeets.cards.animator.ultrarare.NivaLada;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;

import java.util.ArrayList;

public class Chaika extends AnimatorLoadout
{
    public Chaika()
    {
        super(Synergies.Chaika);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_HitsugiNoChaika.ID);
            startingDeck.add(Defend_HitsugiNoChaika.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(AcuraAkari.ID);
            startingDeck.add(Gillette.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetSymbolicCardID()
    {
        return ChaikaTrabant.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        if (ultraRare == null)
        {
            ultraRare = new NivaLada();
        }

        return ultraRare;
    }
}

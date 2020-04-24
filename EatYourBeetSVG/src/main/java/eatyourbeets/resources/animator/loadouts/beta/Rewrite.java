package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_HitsugiNoChaika;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_HitsugiNoChaika;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.AcuraAkari;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.ChaikaTrabant;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.Gillette;
import eatyourbeets.cards.animator.ultrarare.NivaLada;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

import java.util.ArrayList;

public class Rewrite extends AnimatorLoadout
{
    public Rewrite()
    {
        super(Synergies.Rewrite);
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

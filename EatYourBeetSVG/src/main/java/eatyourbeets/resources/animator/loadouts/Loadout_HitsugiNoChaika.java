package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.HitsugiNoChaika.*;
import eatyourbeets.cards.animator.ultrarare.NivaLada;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_HitsugiNoChaika extends AnimatorLoadout
{
    public Loadout_HitsugiNoChaika()
    {
        super(CardSeries.HitsugiNoChaika);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Gillette.DATA, 4);
        AddStarterCard(ChaikaBohdan.DATA, 5);
        AddStarterCard(AcuraTooru.DATA, 5);
        AddStarterCard(Layla.DATA, 6);
        AddStarterCard(AcuraAkari.DATA, 7);
        AddStarterCard(Viivi.DATA, 8);
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

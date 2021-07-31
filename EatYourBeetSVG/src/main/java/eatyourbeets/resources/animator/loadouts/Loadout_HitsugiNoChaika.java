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
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(Gillette.DATA, 4);
        AddToSpecialSlots(ChaikaBohdan.DATA, 5);
        AddToSpecialSlots(AcuraTooru.DATA, 5);
        AddToSpecialSlots(Layla.DATA, 6);
        AddToSpecialSlots(AcuraAkari.DATA, 7);
        AddToSpecialSlots(Viivi.DATA, 8);
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

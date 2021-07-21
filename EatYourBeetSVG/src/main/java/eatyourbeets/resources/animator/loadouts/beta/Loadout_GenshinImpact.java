package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.GenshinImpact.*;
import eatyourbeets.cards.animator.beta.ultrarare.Traveler;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_GenshinImpact extends AnimatorLoadout
{
    public Loadout_GenshinImpact()
    {
        super(CardSeries.GenshinImpact);
        IsBeta = true;
    }

    @Override
    public void InitializeSlots()
    {
        super.InitializeSlots();

        AddToSpecialSlots(Amber.DATA, 4);
        AddToSpecialSlots(Noelle.DATA, 4);
        AddToSpecialSlots(KaeyaAlberich.DATA, 5);
        AddToSpecialSlots(LisaMinci.DATA, 6);
        AddToSpecialSlots(Fischl.DATA, 8);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Venti.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Traveler.DATA;
    }
}

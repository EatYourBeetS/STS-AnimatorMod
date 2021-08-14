package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.Overlord.*;
import eatyourbeets.cards.animator.ultrarare.SirTouchMe;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Overlord extends AnimatorLoadout
{
    public Loadout_Overlord()
    {
        super(CardSeries.Overlord);
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(Demiurge.DATA, 5);
        AddToSpecialSlots(Cocytus.DATA, 5);
        AddToSpecialSlots(PandorasActor.DATA, 6);
        AddToSpecialSlots(Sebas.DATA, 6);
        AddToSpecialSlots(NarberalGamma.DATA, 7);
        AddToSpecialSlots(Shalltear.DATA, 8);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Ainz.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SirTouchMe.DATA;
    }
}

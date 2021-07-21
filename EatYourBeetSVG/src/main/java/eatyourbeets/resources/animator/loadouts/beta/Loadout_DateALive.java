package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.DateALive.*;
import eatyourbeets.cards.animator.beta.ultrarare.MioTakamiya;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_DateALive extends AnimatorLoadout {

    public Loadout_DateALive()
    {
        super(CardSeries.DateALive);

        IsBeta = true;
    }

    @Override
    public void InitializeSlots()
    {
        super.InitializeSlots();

        AddToSpecialSlots(ShidoItsuka.DATA, 5);
        AddToSpecialSlots(YamaiSisters.DATA, 4);
        AddToSpecialSlots(Mayuri.DATA, 5);
        AddToSpecialSlots(ReineMurasame.DATA, 8);
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

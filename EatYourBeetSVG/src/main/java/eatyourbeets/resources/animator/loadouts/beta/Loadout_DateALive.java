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
    public void AddStarterCards()
    {
        AddGenericStarters();

        AddStarterCard(ShidoItsuka.DATA, 5);
        AddStarterCard(Mayuri.DATA, 5);
        AddStarterCard(YamaiSisters.DATA, 5);
        AddStarterCard(ReineMurasame.DATA, 7);
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

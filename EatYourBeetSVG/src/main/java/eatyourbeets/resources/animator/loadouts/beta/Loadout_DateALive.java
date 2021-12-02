package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.curse.Curse_Depression;
import eatyourbeets.cards.animator.beta.series.DateALive.*;
import eatyourbeets.cards.animator.beta.ultrarare.MioTakamiya;
import eatyourbeets.cards.animator.curse.Curse_Clumsy;
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
        AddStarterCard(YamaiSisters.DATA, 4);
        AddStarterCard(ShidoItsuka.DATA, 5);
        AddStarterCard(Mayuri.DATA, 5);
        AddStarterCard(NiaHonjou.DATA, 9);
        AddStarterCard(KotoriItsuka.DATA, 9);
        AddStarterCard(ReineMurasame.DATA, 11);
        AddStarterCard(Curse_Depression.DATA, -6);
        AddStarterCard(Curse_Clumsy.DATA, -3);
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

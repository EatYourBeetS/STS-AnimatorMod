package pinacolada.resources.pcl.loadouts.beta;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Clumsy;
import pinacolada.cards.pcl.curse.Curse_Depression;
import pinacolada.cards.pcl.series.DateALive.*;
import pinacolada.cards.pcl.ultrarare.MioTakamiya;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_DateALive extends PCLLoadout {

    public Loadout_DateALive()
    {
        super(CardSeries.DateALive);

        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(YamaiSisters.DATA, 4);
        AddStarterCard(KotoriItsuka.DATA, 6);
        AddStarterCard(ShidoItsuka.DATA, 7);
        AddStarterCard(NiaHonjou.DATA, 9);
        AddStarterCard(RinneSonogami.DATA, 9);
        AddStarterCard(ReineMurasame.DATA, 11);
        AddStarterCard(Curse_Depression.DATA, -6);
        AddStarterCard(Curse_Clumsy.DATA, -3);
    }

    @Override
    public PCLCardData GetSymbolicCard() {
        return TohkaYatogami.DATA;
    }

    @Override
    public PCLCardData GetUltraRare() {
        return MioTakamiya.DATA;
    }
}

package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
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
        AddStarterCard(ShidoItsuka.DATA, 6);
        AddStarterCard(KotoriItsuka.DATA, 6);
        AddStarterCard(Ren.DATA, 9);
        AddStarterCard(NiaHonjou.DATA, 11);
        AddStarterCard(MikuIzayoi.DATA, 11);
        AddStarterCard(ReineMurasame.DATA, 12);
        AddStarterCard(Mayuri.DATA, 14);
        AddStarterCard(RinneSonogami.DATA, 14);
        AddStarterCard(YoshinoHimekawa.DATA, 15);
        AddStarterCard(Natsumi.DATA, 15);
        AddStarterCard(MukuroHoshimiya.DATA, 20);
        AddStarterCard(KurumiTokisaki.DATA, 33);
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

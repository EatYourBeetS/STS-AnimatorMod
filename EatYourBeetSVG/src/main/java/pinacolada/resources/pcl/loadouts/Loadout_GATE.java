package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.GATE.*;
import pinacolada.cards.pcl.ultrarare.Giselle;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_GATE extends PCLLoadout
{
    public Loadout_GATE()
    {
        super(CardSeries.GATE);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(YaoHaDucy.DATA, 5);
        AddStarterCard(Kuribayashi.DATA, 5);
        AddStarterCard(SherryTueli.DATA, 6);
        AddStarterCard(TukaLunaMarceau.DATA, 6);
        AddStarterCard(MariKurokawa.DATA, 7);
        AddStarterCard(Tyuule.DATA, 8);
        AddStarterCard(LeleiLaLalena.DATA, 9);
        AddStarterCard(RoryMercury.DATA, 9);
        AddStarterCard(Bozes.DATA, 11);
        AddStarterCard(CatoElAltestan.DATA, 12);
        AddStarterCard(ItamiYouji.DATA, 25);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return RoryMercury.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Giselle.DATA;
    }
}

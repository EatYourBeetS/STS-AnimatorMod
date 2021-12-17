package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Injury;
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
        AddStarterCard(Kuribayashi.DATA, 5);
        AddStarterCard(YaoHaDucy.DATA, 6);
        AddStarterCard(TukaLunaMarceau.DATA, 6);
        AddStarterCard(MariKurokawa.DATA, 7);
        AddStarterCard(RoryMercury.DATA, 8);
        AddStarterCard(Bozes.DATA, 11);
        AddStarterCard(ItamiYouji.DATA, 25);
        AddStarterCard(Curse_Injury.DATA, -4);
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

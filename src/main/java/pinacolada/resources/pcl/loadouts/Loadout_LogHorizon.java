package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.LogHorizon.*;
import pinacolada.cards.pcl.ultrarare.Kanami;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_LogHorizon extends PCLLoadout
{
    public Loadout_LogHorizon()
    {
        super(CardSeries.LogHorizon);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(HousakiTohya.DATA, 4);
        AddStarterCard(HousakiMinori.DATA, 5);
        AddStarterCard(Serara.DATA, 6);
        AddStarterCard(Marielle.DATA, 7);
        AddStarterCard(Naotsugu.DATA, 8);
        AddStarterCard(IsuzuTonan.DATA, 9);
        AddStarterCard(RundelhausCode.DATA, 11);
        AddStarterCard(Nyanta.DATA, 11);
        AddStarterCard(Rayneshia.DATA, 13);
        AddStarterCard(Shiroe.DATA, 20);
        AddStarterCard(Akatsuki.DATA, 22);
        AddStarterCard(Soujiro.DATA, 24);
        AddStarterCard(Krusty.DATA, 25);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Akatsuki.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Kanami.DATA;
    }
}

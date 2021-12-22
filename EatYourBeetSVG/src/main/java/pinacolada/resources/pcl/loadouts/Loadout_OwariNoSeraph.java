package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Clumsy;
import pinacolada.cards.pcl.curse.Curse_Parasite;
import pinacolada.cards.pcl.series.OwariNoSeraph.*;
import pinacolada.cards.pcl.ultrarare.HiiragiMahiru;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_OwariNoSeraph extends PCLLoadout
{
    public Loadout_OwariNoSeraph()
    {
        super(CardSeries.OwariNoSeraph);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Shigure.DATA, 5);
        AddStarterCard(KimizugiShiho.DATA, 6);
        AddStarterCard(Yoichi.DATA, 6);
        AddStarterCard(Mitsuba.DATA, 6);
        AddStarterCard(Shinoa.DATA, 7);
        AddStarterCard(CrowleyEusford.DATA, 8);
        AddStarterCard(Mikaela.DATA, 8);
        AddStarterCard(Yuuichirou.DATA, 8);
        AddStarterCard(LestKarr.DATA, 10);
        AddStarterCard(HiiragiShinya.DATA, 13);
        AddStarterCard(CrowleyEusford.DATA, 13);
        AddStarterCard(HiiragiKureto.DATA, 20);
        AddStarterCard(Guren.DATA, 32);
        AddStarterCard(Curse_Parasite.DATA, -7);
        AddStarterCard(Curse_Clumsy.DATA, -3);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Yuuichirou.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return HiiragiMahiru.DATA;
    }
}

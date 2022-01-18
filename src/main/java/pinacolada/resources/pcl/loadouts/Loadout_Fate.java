package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.Fate.*;
import pinacolada.cards.pcl.ultrarare.JeanneDArc;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_Fate extends PCLLoadout
{
    public Loadout_Fate()
    {
        super(CardSeries.Fate);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Assassin.DATA, 4);
        AddStarterCard(Alexander.DATA, 6);
        AddStarterCard(Lancer.DATA, 7);
        AddStarterCard(MatouShinji.DATA, 8);
        AddStarterCard(Rider.DATA, 8);
        AddStarterCard(RinTohsaka.DATA, 8);
        AddStarterCard(Berserker.DATA, 10);
        AddStarterCard(MatouSakura.DATA, 10);
        AddStarterCard(EmiyaShirou.DATA, 11);
        AddStarterCard(RinTohsaka.DATA, 13);
        AddStarterCard(EmiyaKiritsugu.DATA, 16);
        AddStarterCard(Caster.DATA, 17);
        AddStarterCard(KireiKotomine.DATA, 23);
        AddStarterCard(Saber.DATA, 25);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return EmiyaShirou.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return JeanneDArc.DATA;
    }
}

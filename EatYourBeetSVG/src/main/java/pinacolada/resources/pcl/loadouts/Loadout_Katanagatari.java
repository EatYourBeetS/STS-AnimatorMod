package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Injury;
import pinacolada.cards.pcl.curse.Curse_Pain;
import pinacolada.cards.pcl.series.Katanagatari.*;
import pinacolada.cards.pcl.ultrarare.ShikizakiKiki;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_Katanagatari extends PCLLoadout
{
    public Loadout_Katanagatari()
    {
        super(CardSeries.Katanagatari);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Konayuki.DATA, 5);
        AddStarterCard(TsurugaMeisai.DATA, 5);
        AddStarterCard(ZankiKiguchi.DATA, 6);
        AddStarterCard(Emonzaemon.DATA, 6);
        AddStarterCard(Azekura.DATA, 8);
        AddStarterCard(UneriGinkaku.DATA, 7);
        AddStarterCard(Shichika.DATA, 14);
        AddStarterCard(Togame.DATA, 10);
        AddStarterCard(Curse_Injury.DATA, -4);
        AddStarterCard(Curse_Pain.DATA, -9);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return HigakiRinne.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return ShikizakiKiki.DATA;
    }
}

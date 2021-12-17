package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Injury;
import pinacolada.cards.pcl.curse.Curse_Parasite;
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
        AddStarterCard(Rider.DATA, 8);
        AddStarterCard(RinTohsaka.DATA, 8);
        AddStarterCard(Berserker.DATA, 10);
        AddStarterCard(EmiyaShirou.DATA, 11);
        AddStarterCard(Saber.DATA, 24);
        AddStarterCard(Curse_Injury.DATA, -4);
        AddStarterCard(Curse_Parasite.DATA, -7);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Saber.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return JeanneDArc.DATA;
    }
}

package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Injury;
import pinacolada.cards.pcl.curse.Curse_SearingBurn;
import pinacolada.cards.pcl.series.OnePunchMan.*;
import pinacolada.cards.pcl.ultrarare.SeriousSaitama;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_OnePunchMan extends PCLLoadout
{
    public Loadout_OnePunchMan()
    {
        super(CardSeries.OnePunchMan);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(SilverFang.DATA, 5);
        AddStarterCard(MetalBat.DATA, 5);
        AddStarterCard(MumenRider.DATA, 5);
        AddStarterCard(Genos.DATA, 6);
        AddStarterCard(King.DATA, 7);
        AddStarterCard(Melzalgald.DATA, 12);
        AddStarterCard(Saitama.DATA, 16);
        AddStarterCard(Curse_Injury.DATA, -4);
        AddStarterCard(Curse_SearingBurn.DATA, -6);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Saitama.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return SeriousSaitama.DATA;
    }
}

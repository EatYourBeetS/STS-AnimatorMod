package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Injury;
import pinacolada.cards.pcl.series.GoblinSlayer.*;
import pinacolada.cards.pcl.ultrarare.Hero;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_GoblinSlayer extends PCLLoadout
{
    public Loadout_GoblinSlayer()
    {
        super(CardSeries.GoblinSlayer);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(HighElfArcher.DATA, 5);
        AddStarterCard(DwarfShaman.DATA, 6);
        AddStarterCard(LizardPriest.DATA, 6);
        AddStarterCard(Priestess.DATA, 7);
        AddStarterCard(Spearman.DATA, 8);
        AddStarterCard(Witch.DATA, 9);
        AddStarterCard(GoblinSlayer.DATA, 25);
        AddStarterCard(Curse_Injury.DATA, -4);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return pinacolada.cards.pcl.series.GoblinSlayer.GoblinSlayer.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Hero.DATA;
    }
}

package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.FullmetalAlchemist.*;
import pinacolada.cards.pcl.ultrarare.Truth;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_FullmetalAlchemist extends PCLLoadout
{
    public Loadout_FullmetalAlchemist()
    {
        super(CardSeries.FullmetalAlchemist);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(ElricAlphonse.DATA, 4);
        AddStarterCard(MaesHughes.DATA, 4);
        AddStarterCard(ElricEdward.DATA, 4);
        AddStarterCard(RizaHawkeye.DATA, 5);
        AddStarterCard(Gluttony.DATA, 8);
        AddStarterCard(Lust.DATA, 8);
        AddStarterCard(Sloth.DATA, 10);
        AddStarterCard(Pride.DATA, 11);
        AddStarterCard(RoyMustang.DATA, 12);
        AddStarterCard(Scar.DATA, 12);

    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return RoyMustang.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Truth.DATA;
    }
}

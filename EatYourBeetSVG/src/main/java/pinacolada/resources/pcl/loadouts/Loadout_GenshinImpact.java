package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_SearingBurn;
import pinacolada.cards.pcl.series.GenshinImpact.*;
import pinacolada.cards.pcl.ultrarare.Dainsleif;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_GenshinImpact extends PCLLoadout
{
    public Loadout_GenshinImpact()
    {
        super(CardSeries.GenshinImpact);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Amber.DATA, 4);
        AddStarterCard(Noelle.DATA, 5);
        AddStarterCard(LisaMinci.DATA, 6);
        AddStarterCard(KaeyaAlberich.DATA, 7);
        AddStarterCard(Fischl.DATA, 10);
        AddStarterCard(Bennett.DATA, 10);
        AddStarterCard(Tartaglia.DATA, 10);
        AddStarterCard(Keqing.DATA, 14);
        AddStarterCard(Klee.DATA, 14);
        AddStarterCard(HuTao.DATA, 14);
        AddStarterCard(JeanGunnhildr.DATA, 20);
        AddStarterCard(Venti.DATA, 25);
        AddStarterCard(Curse_SearingBurn.DATA, -6);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Venti.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Dainsleif.DATA;
    }
}

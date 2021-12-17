package pinacolada.resources.pcl.loadouts.beta;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Injury;
import pinacolada.cards.pcl.curse.Curse_Pain;
import pinacolada.cards.pcl.series.Rewrite.*;
import pinacolada.cards.pcl.ultrarare.SakuraKashima;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_Rewrite extends PCLLoadout
{
    public Loadout_Rewrite()
    {
        super(CardSeries.Rewrite);

        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(YoshinoHaruhiko.DATA, 4);
        AddStarterCard(ShizuruNakatsu.DATA, 4);
        AddStarterCard(Shimako.DATA, 5);
        AddStarterCard(SougenEsaka.DATA, 6);
        AddStarterCard(ChihayaOhtori.DATA, 8);
        AddStarterCard(SakuyaOhtori.DATA, 11);
        AddStarterCard(KotarouTennouji.DATA, 16);
        AddStarterCard(Curse_Injury.DATA, -4);
        AddStarterCard(Curse_Pain.DATA, -9);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Kagari.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return SakuraKashima.DATA;
    }
}

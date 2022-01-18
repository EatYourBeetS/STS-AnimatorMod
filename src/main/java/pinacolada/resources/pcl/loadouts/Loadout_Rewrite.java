package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
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
        AddStarterCard(ShizuruNakatsu.DATA, 4);
        AddStarterCard(Shimako.DATA, 4);
        AddStarterCard(Midou.DATA, 4);
        AddStarterCard(SougenEsaka.DATA, 6);
        AddStarterCard(Chibimoth.DATA, 8);
        AddStarterCard(ChihayaOhtori.DATA, 8);
        AddStarterCard(YoshinoHaruhiko.DATA, 9);
        AddStarterCard(ToukaNishikujou.DATA, 11);
        AddStarterCard(LuciaKonohana.DATA, 11);
        AddStarterCard(SakuyaOhtori.DATA, 11);
        AddStarterCard(KotarouTennouji.DATA, 19);
        AddStarterCard(KotoriKanbe.DATA, 22);
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

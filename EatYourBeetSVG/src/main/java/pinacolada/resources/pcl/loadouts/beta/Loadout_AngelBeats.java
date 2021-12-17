package pinacolada.resources.pcl.loadouts.beta;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Clumsy;
import pinacolada.cards.pcl.curse.Curse_Depression;
import pinacolada.cards.pcl.series.AngelBeats.*;
import pinacolada.cards.pcl.ultrarare.TK;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_AngelBeats extends PCLLoadout
{
    public Loadout_AngelBeats()
    {
        super(CardSeries.AngelBeats);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Fujimaki.DATA, 4);
        AddStarterCard(HidekiHinata.DATA, 5);
        AddStarterCard(Noda.DATA, 5);
        AddStarterCard(MasamiIwasawa.DATA, 5);
        AddStarterCard(Yusa.DATA, 6);
        AddStarterCard(YuzuruOtonashi.DATA, 6);
        AddStarterCard(YuriNakamura.DATA, 18);
        AddStarterCard(Curse_Clumsy.DATA, -3);
        AddStarterCard(Curse_Depression.DATA, -6);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return KanadeTachibana.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return TK.DATA;
    }
}

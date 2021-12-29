package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.Bleach.*;
import pinacolada.cards.pcl.ultrarare.SosukeAizen;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_Bleach extends PCLLoadout
{
    public Loadout_Bleach()
    {
        super(CardSeries.Bleach);

        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(RenjiAbarai.DATA, 4);
        AddStarterCard(YasutoraSado.DATA, 5);
        AddStarterCard(UryuuIshida.DATA, 5);
        AddStarterCard(IkkakuMadarame.DATA, 5);
        AddStarterCard(OrihimeInoue.DATA, 6);
        AddStarterCard(IchigoKurosaki.DATA, 6);
        AddStarterCard(MayuriKurotsuchi.DATA, 7);
        AddStarterCard(IsshinKurosaki.DATA, 10);
        AddStarterCard(RukiaKuchiki.DATA, 13);
        AddStarterCard(GinIchimaru.DATA, 13);
        AddStarterCard(KanameTousen.DATA, 14);
        AddStarterCard(SajinKomamura.DATA, 14);
        AddStarterCard(ToushirouHitsugaya.DATA, 20);
        AddStarterCard(ByakuyaKuchiki.DATA, 20);
    }


    @Override
    public PCLCardData GetSymbolicCard()
    {
        return IchigoKurosaki.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return SosukeAizen.DATA;
    }
}

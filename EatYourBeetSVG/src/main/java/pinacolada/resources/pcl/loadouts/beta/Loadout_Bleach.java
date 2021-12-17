package pinacolada.resources.pcl.loadouts.beta;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Injury;
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
        AddStarterCard(OrihimeInoue.DATA, 4);
        AddStarterCard(YasutoraSado.DATA, 5);
        AddStarterCard(UryuuIshida.DATA, 5);
        AddStarterCard(IchigoKurosaki.DATA, 7);
        AddStarterCard(RukiaKuchiki.DATA, 11);
        AddStarterCard(ByakuyaKuchiki.DATA, 20);
        AddStarterCard(Curse_Injury.DATA, -4);
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

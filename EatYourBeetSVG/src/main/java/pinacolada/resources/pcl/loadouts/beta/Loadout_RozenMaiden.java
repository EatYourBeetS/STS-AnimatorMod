package pinacolada.resources.pcl.loadouts.beta;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Decay;
import pinacolada.cards.pcl.series.RozenMaiden.*;
import pinacolada.cards.pcl.ultrarare.Kirakishou;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_RozenMaiden extends PCLLoadout
{
    public Loadout_RozenMaiden()
    {
        super(CardSeries.RozenMaiden);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(TomoeKashiwaba.DATA, 5);
        AddStarterCard(Hinaichigo.DATA, 5);
        AddStarterCard(MitsuKusabue.DATA, 7);
        AddStarterCard(Souseiseki.DATA, 8);
        AddStarterCard(Shinku.DATA, 8);
        AddStarterCard(JunSakurada.DATA, 11);
        AddStarterCard(Curse_Decay.DATA, -7);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Shinku.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Kirakishou.DATA;
    }
}

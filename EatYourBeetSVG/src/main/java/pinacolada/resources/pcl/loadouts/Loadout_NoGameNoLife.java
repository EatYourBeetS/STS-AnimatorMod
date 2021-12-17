package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Clumsy;
import pinacolada.cards.pcl.curse.Curse_Slumber;
import pinacolada.cards.pcl.series.NoGameNoLife.*;
import pinacolada.cards.pcl.ultrarare.Azriel;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_NoGameNoLife extends PCLLoadout
{
    public Loadout_NoGameNoLife()
    {
        super(CardSeries.NoGameNoLife);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(DolaCouronne.DATA, 5);
        AddStarterCard(IzunaHatsuse.DATA, 5);
        AddStarterCard(ChlammyZell.DATA, 7);
        AddStarterCard(DolaSchwi.DATA, 9);
        AddStarterCard(DolaStephanie.DATA, 10);
        AddStarterCard(Tet.DATA, 14);
        AddStarterCard(Sora.DATA, 16);
        AddStarterCard(Curse_Clumsy.DATA, -3);
        AddStarterCard(Curse_Slumber.DATA, -7);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Sora.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Azriel.DATA;
    }
}

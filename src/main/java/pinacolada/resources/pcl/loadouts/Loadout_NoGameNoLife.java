package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
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
        AddStarterCard(NinaClive.DATA, 7);
        AddStarterCard(ChlammyZell.DATA, 8);
        AddStarterCard(DolaStephanie.DATA, 9);
        AddStarterCard(EmirEins.DATA, 11);
        AddStarterCard(DolaSchwi.DATA, 11);
        AddStarterCard(Tet.DATA, 14);
        AddStarterCard(Jibril.DATA, 20);
        AddStarterCard(Sora.DATA, 21);
        AddStarterCard(Shiro.DATA, 22);
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

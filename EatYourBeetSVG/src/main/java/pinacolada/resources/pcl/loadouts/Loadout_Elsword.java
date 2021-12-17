package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Doubt;
import pinacolada.cards.pcl.curse.Curse_Injury;
import pinacolada.cards.pcl.series.Elsword.*;
import pinacolada.cards.pcl.ultrarare.Rose;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_Elsword extends PCLLoadout
{
    public Loadout_Elsword()
    {
        super(CardSeries.Elsword);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Aisha.DATA, 5);
        AddStarterCard(Raven.DATA, 5);
        AddStarterCard(Chung.DATA, 5);
        AddStarterCard(Elsword.DATA, 6);
        AddStarterCard(Ciel.DATA, 7);
        AddStarterCard(Ara.DATA, 8);
        AddStarterCard(Rena.DATA, 11);
        AddStarterCard(Curse_Injury.DATA, -4);
        AddStarterCard(Curse_Doubt.DATA, -7);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Eve.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return Rose.DATA;
    }
}

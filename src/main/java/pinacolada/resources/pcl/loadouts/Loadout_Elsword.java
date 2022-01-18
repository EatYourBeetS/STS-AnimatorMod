package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
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
        AddStarterCard(Ara.DATA, 8);
        AddStarterCard(Ciel.DATA, 9);
        AddStarterCard(Rena.DATA, 11);
        AddStarterCard(Add.DATA, 12);
        AddStarterCard(Ain.DATA, 13);
        AddStarterCard(Lu.DATA, 13);
        AddStarterCard(Elesis.DATA, 22);
        AddStarterCard(Noah.DATA, 25);
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

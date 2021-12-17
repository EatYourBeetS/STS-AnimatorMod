package pinacolada.resources.pcl.loadouts;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_Pain;
import pinacolada.cards.pcl.curse.Curse_Writhe;
import pinacolada.cards.pcl.series.Overlord.*;
import pinacolada.cards.pcl.ultrarare.SirTouchMe;
import pinacolada.resources.pcl.misc.PCLLoadout;

public class Loadout_Overlord extends PCLLoadout
{
    public Loadout_Overlord()
    {
        super(CardSeries.Overlord);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(PandorasActor.DATA, 4);
        AddStarterCard(Demiurge.DATA, 5);
        AddStarterCard(Cocytus.DATA, 6);
        AddStarterCard(NarberalGamma.DATA, 7);
        AddStarterCard(Sebas.DATA, 7);
        AddStarterCard(CZDelta.DATA, 8);
        AddStarterCard(AuraBellaFiora.DATA, 11);
        AddStarterCard(MareBelloFiore.DATA, 12);
        AddStarterCard(Curse_Writhe.DATA, -5);
        AddStarterCard(Curse_Pain.DATA, -8);
    }

    @Override
    public PCLCardData GetSymbolicCard()
    {
        return Ainz.DATA;
    }

    @Override
    public PCLCardData GetUltraRare()
    {
        return SirTouchMe.DATA;
    }
}

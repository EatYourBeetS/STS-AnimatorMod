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

        AddStarterCard(Demiurge.DATA, 5);
        AddStarterCard(Cocytus.DATA, 6);
        AddStarterCard(PandorasActor.DATA, 7);
        AddStarterCard(Sebas.DATA, 7);
        AddStarterCard(CZDelta.DATA, 8);
        AddStarterCard(NarberalGamma.DATA, 8);
        AddStarterCard(Entoma.DATA, 9);
        AddStarterCard(AuraBellaFiora.DATA, 9);
        AddStarterCard(Evileye.DATA, 10);
        AddStarterCard(Gargantua.DATA, 12);
        AddStarterCard(MareBelloFiore.DATA, 12);
        AddStarterCard(Victim.DATA, 17);
        AddStarterCard(Shalltear.DATA, 21);
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

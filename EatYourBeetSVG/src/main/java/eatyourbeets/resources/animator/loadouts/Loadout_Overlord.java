package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.Overlord.*;
import eatyourbeets.cards.animator.ultrarare.SirTouchMe;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Overlord extends AnimatorLoadout
{
    public Loadout_Overlord()
    {
        super(CardSeries.Overlord);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Cocytus.DATA, 6);
        AddStarterCard(Entoma.DATA, 9);
        AddStarterCard(CZDelta.DATA, 8);
        AddStarterCard(Demiurge.DATA, 4);
        AddStarterCard(PandorasActor.DATA, 6);

        AddStarterCard(Sebas.DATA, 7);
        AddStarterCard(MareBelloFiore.DATA, 5);
        AddStarterCard(NarberalGamma.DATA, 7);
        AddStarterCard(GazefStronoff.DATA, 9);
        AddStarterCard(Evileye.DATA, 12);
        AddStarterCard(AuraBellaFiora.DATA, 9);

        AddStarterCard(Shalltear.DATA, 20);
        AddStarterCard(Albedo.DATA, 22);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Ainz.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SirTouchMe.DATA;
    }
}

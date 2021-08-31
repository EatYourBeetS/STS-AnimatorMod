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
        AddStarterCard(Demiurge.DATA, 6);
        AddStarterCard(Cocytus.DATA, 6);
        AddStarterCard(PandorasActor.DATA, 6);
        AddStarterCard(NarberalGamma.DATA, 7);
        AddStarterCard(Sebas.DATA, 8);
        AddStarterCard(CZDelta.DATA, 8);
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

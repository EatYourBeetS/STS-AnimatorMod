package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.LogHorizon.*;
import eatyourbeets.cards.animator.ultrarare.Kanami;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_LogHorizon extends AnimatorLoadout
{
    public Loadout_LogHorizon()
    {
        super(CardSeries.LogHorizon);
    }

    @Override
    public void AddStarterCards()
    {
        AddGenericStarters();

        AddStarterCard(HousakiTohya.DATA, 5);
        AddStarterCard(HousakiMinori.DATA, 5);
        AddStarterCard(Serara.DATA, 6);
        AddStarterCard(Marielle.DATA, 7);
        AddStarterCard(Naotsugu.DATA, 8);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Akatsuki.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Kanami.DATA;
    }
}

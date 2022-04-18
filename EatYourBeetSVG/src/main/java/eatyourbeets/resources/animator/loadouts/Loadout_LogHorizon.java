package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards_beta.special.Kanami;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards_beta.LogHorizon.*;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_LogHorizon extends AnimatorLoadout
{
    public Loadout_LogHorizon()
    {
        super(CardSeries.LogHorizon);

        IsEnabled = false;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(HousakiTohya.DATA, 4);
        AddStarterCard(HousakiMinori.DATA, 4);
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

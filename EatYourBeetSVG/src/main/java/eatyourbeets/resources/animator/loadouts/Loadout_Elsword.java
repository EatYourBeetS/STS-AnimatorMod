package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.Elsword.*;
import eatyourbeets.cards.animator.ultrarare.Rose;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Elsword extends AnimatorLoadout
{
    public Loadout_Elsword()
    {
        super(CardSeries.Elsword);
    }

    @Override
    public void AddStarterCards()
    {
        AddGenericStarters();

        AddStarterCard(Ara.DATA, 5);
        AddStarterCard(Raven.DATA, 5);
        AddStarterCard(Chung.DATA, 6);
        AddStarterCard(Elsword.DATA, 7);
        AddStarterCard(Ciel.DATA, 7);
        AddStarterCard(Rena.DATA, 9);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Eve.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Rose.DATA;
    }
}

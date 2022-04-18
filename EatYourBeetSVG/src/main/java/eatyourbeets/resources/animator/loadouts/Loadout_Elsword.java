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
        AddStarterCard(Elsword.DATA, 7);
        AddStarterCard(Ciel.DATA, 7);
        AddStarterCard(Chung.DATA, 9);
        AddStarterCard(Raven.DATA, 6);
        AddStarterCard(Ara.DATA, 5);

        AddStarterCard(Aisha.DATA, 8);
        AddStarterCard(Ain.DATA, 13);
        AddStarterCard(Rena.DATA, 9);
        AddStarterCard(Lu.DATA, 16);

        AddStarterCard(Elesis.DATA, 22);
        AddStarterCard(Noah.DATA, 23);
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

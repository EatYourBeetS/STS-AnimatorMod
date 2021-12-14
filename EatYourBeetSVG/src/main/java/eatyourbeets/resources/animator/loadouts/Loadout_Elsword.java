package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.Curse_Doubt;
import eatyourbeets.cards.animator.curse.Curse_Injury;
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

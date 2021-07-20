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
    public void InitializeSlots()
    {
        super.InitializeSlots();

        AddToSpecialSlots(Ara.DATA, 4);
        AddToSpecialSlots(Chung.DATA, 5);
        AddToSpecialSlots(Raven.DATA, 5);
        AddToSpecialSlots(Elsword.DATA, 6);
        AddToSpecialSlots(Rena.DATA, 9);
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

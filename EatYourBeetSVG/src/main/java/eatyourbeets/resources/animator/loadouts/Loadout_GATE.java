package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.GATE.*;
import eatyourbeets.cards.animator.ultrarare.Giselle;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_GATE extends AnimatorLoadout
{
    public Loadout_GATE()
    {
        super(CardSeries.GATE);
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(TukaLunaMarceau.DATA, 4);
        AddToSpecialSlots(Kuribayashi.DATA, 5);
        AddToSpecialSlots(YaoHaDucy.DATA, 5);
        AddToSpecialSlots(ShunyaKengun.DATA, 7);
        AddToSpecialSlots(RoryMercury.DATA, 7);
        AddToSpecialSlots(Bozes.DATA, 11);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return RoryMercury.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Giselle.DATA;
    }
}

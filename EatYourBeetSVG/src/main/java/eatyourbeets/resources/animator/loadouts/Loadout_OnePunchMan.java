package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.OnePunchMan.*;
import eatyourbeets.cards.animator.ultrarare.SeriousSaitama;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_OnePunchMan extends AnimatorLoadout
{
    public Loadout_OnePunchMan()
    {
        super(CardSeries.OnePunchMan);
    }

    @Override
    public void InitializeSlots()
    {
        super.InitializeSlots();

        AddToSpecialSlots(Genos.DATA, 4);
        AddToSpecialSlots(SilverFang.DATA, 4);
        AddToSpecialSlots(MumenRider.DATA, 5);
        AddToSpecialSlots(Geryuganshoop.DATA, 6);
        AddToSpecialSlots(Saitama.DATA, 16);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Saitama.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SeriousSaitama.DATA;
    }
}
package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.Curse_Greed;
import eatyourbeets.cards.animator.series.Konosuba.*;
import eatyourbeets.cards.animator.ultrarare.Chomusuke;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Konosuba extends AnimatorLoadout
{
    public Loadout_Konosuba()
    {
        super(CardSeries.Konosuba);
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(Aqua.DATA, 4);
        AddToSpecialSlots(Kazuma.DATA, 5);
        AddToSpecialSlots(Chris.DATA, 5);
        AddToSpecialSlots(Mitsurugi.DATA, 6);
        AddToSpecialSlots(Vanir.DATA, 7);
        AddToSpecialSlots(YunYun.DATA, 9);
        AddToSpecialSlots(Curse_Greed.DATA, -7);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Megumin.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Chomusuke.DATA;
    }
}

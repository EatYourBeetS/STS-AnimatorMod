package eatyourbeets.resources.animator.loadouts;

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
    public void InitializeSlots()
    {
        super.InitializeSlots();

        AddSeriesItem(Kazuma.DATA, 4);
        AddSeriesItem(Aqua.DATA, 4);
        AddSeriesItem(Mitsurugi.DATA, 5);
        AddSeriesItem(Chris.DATA, 5);
        AddSeriesItem(Vanir.DATA, 7);
        AddSeriesItem(YunYun.DATA, 9);
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

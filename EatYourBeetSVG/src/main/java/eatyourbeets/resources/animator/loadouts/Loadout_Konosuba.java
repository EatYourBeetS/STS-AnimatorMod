package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.Curse_Greed;
import eatyourbeets.cards.animator.series.Konosuba.*;
import eatyourbeets.cards.animator.series.Konosuba.Chris;
import eatyourbeets.cards.animator.status.Status_Slimed;
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
    public void AddStarterCards()
    {
        AddStarterCard(Aqua.DATA, 4);
        AddStarterCard(Kazuma.DATA, 5);
        AddStarterCard(Chris.DATA, 5);
        AddStarterCard(Mitsurugi.DATA, 6);
        AddStarterCard(Vanir.DATA, 7);
        AddStarterCard(YunYun.DATA, 9);
        AddStarterCard(Status_Slimed.DATA, -3);
        AddStarterCard(Curse_Greed.DATA, -7);
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

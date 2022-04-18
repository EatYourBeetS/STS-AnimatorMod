package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.common.Curse_Greed;
import eatyourbeets.cards.animator.series.Konosuba.*;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.animator.ultrarare.Chomusuke;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Konosuba extends AnimatorLoadout
{
    private static final EYBCardTooltip sylviaTooltip = new EYBCardTooltip("", "");

    public Loadout_Konosuba()
    {
        super(CardSeries.Konosuba);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Mitsurugi.DATA, 6);
        AddStarterCard(Kazuma.DATA, 7);
        AddStarterCard(Verdia.DATA, 8);
        AddStarterCard(Vanir.DATA, 8);
        AddStarterCard(Sylvia.DATA, 8);

        AddStarterCard(YunYun.DATA, 9);
        AddStarterCard(Chris.DATA, 6);
        AddStarterCard(Megumin.DATA, 14);
        AddStarterCard(Aqua.DATA, 6);

        AddStarterCard(Wiz.DATA, 22);
        AddStarterCard(Eris.DATA, 24);

        AddStarterCard(Status_Slimed.DATA, -5);
        AddStarterCard(Curse_Greed.DATA, -8);
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

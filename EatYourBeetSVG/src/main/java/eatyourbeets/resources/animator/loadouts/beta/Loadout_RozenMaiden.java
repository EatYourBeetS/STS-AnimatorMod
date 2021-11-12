package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.RozenMaiden.*;
import eatyourbeets.cards.animator.beta.special.Suigintou_BlackFeather;
import eatyourbeets.cards.animator.beta.ultrarare.Kirakishou;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_RozenMaiden extends AnimatorLoadout
{
    public Loadout_RozenMaiden()
    {
        super(CardSeries.RozenMaiden);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(TomoeKashiwaba.DATA, 5);
        AddStarterCard(Hinaichigo.DATA, 5);
        AddStarterCard(MitsuKusabue.DATA, 7);
        AddStarterCard(Souseiseki.DATA, 8);
        AddStarterCard(Shinku.DATA, 8);
        AddStarterCard(JunSakurada.DATA, 11);
        AddStarterCard(Suigintou_BlackFeather.DATA, -7);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Shinku.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Kirakishou.DATA;
    }
}

package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.AngelBeats.*;
import eatyourbeets.cards.animator.beta.ultrarare.AngelAlter;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_AngelBeats extends AnimatorLoadout
{
    public Loadout_AngelBeats()
    {
        super(CardSeries.AngelBeats);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(HidekiHinata.DATA, 4);
        AddStarterCard(MasamiIwasawa.DATA, 5);
        AddStarterCard(Yusa.DATA, 4);
        AddStarterCard(Noda.DATA, 5);
        AddStarterCard(YuzuruOtonashi.DATA, 8);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return KanadeTachibana.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return AngelAlter.DATA;
    }
}

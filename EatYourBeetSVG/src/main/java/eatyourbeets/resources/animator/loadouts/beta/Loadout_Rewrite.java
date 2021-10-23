package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.Rewrite.*;
import eatyourbeets.cards.animator.beta.ultrarare.SakuraKashima;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Rewrite extends AnimatorLoadout
{
    public Loadout_Rewrite()
    {
        super(CardSeries.Rewrite);

        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddGenericStarters();

        AddStarterCard(ShizuruNakatsu.DATA, 5);
        AddStarterCard(Shimako.DATA, 5);
        AddStarterCard(SougenEsaka.DATA, 6);
        AddStarterCard(YoshinoHaruhiko.DATA, 7);
        AddStarterCard(ChihayaOhtori.DATA, 8);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Kagari.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SakuraKashima.DATA;
    }
}

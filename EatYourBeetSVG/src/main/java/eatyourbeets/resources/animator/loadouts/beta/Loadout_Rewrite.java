package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.Rewrite.*;
import eatyourbeets.cards.animator.beta.ultrarare.SakuraKashima;
import eatyourbeets.cards.animator.curse.Curse_Clumsy;
import eatyourbeets.cards.animator.curse.Curse_Injury;
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
        AddStarterCard(YoshinoHaruhiko.DATA, 4);
        AddStarterCard(ShizuruNakatsu.DATA, 4);
        AddStarterCard(Shimako.DATA, 5);
        AddStarterCard(SougenEsaka.DATA, 6);
        AddStarterCard(ChihayaOhtori.DATA, 8);
        AddStarterCard(SakuyaOhtori.DATA, 11);
        AddStarterCard(KotarouTennouji.DATA, 16);
        AddStarterCard(Curse_Injury.DATA, -4);
        AddStarterCard(Curse_Clumsy.DATA, -3);
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

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
    public void AddStarterCards()
    {
        AddStarterCard(MariKurokawa.DATA, 6);
        AddStarterCard(YaoHaDucy.DATA, 6);
        AddStarterCard(Kuribayashi.DATA, 7);
        AddStarterCard(ShunyaKengun.DATA, 7);
        AddStarterCard(TukaLunaMarceau.DATA, 8);

        AddStarterCard(RoryMercury.DATA, 8);
        AddStarterCard(Bozes.DATA, 11);
        AddStarterCard(Tyuule.DATA, 5);
        AddStarterCard(LeleiLaLalena.DATA, 8);
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

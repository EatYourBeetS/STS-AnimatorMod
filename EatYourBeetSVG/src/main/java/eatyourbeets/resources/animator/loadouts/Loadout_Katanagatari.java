package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.Katanagatari.*;
import eatyourbeets.cards.animator.ultrarare.ShikizakiKiki;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Katanagatari extends AnimatorLoadout
{
    public Loadout_Katanagatari()
    {
        super(CardSeries.Katanagatari);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Konayuki.DATA, 5);
        AddStarterCard(TsurugaMeisai.DATA, 5);
        AddStarterCard(ZankiKiguchi.DATA, 6);
        AddStarterCard(Emonzaemon.DATA, 6);
        AddStarterCard(Azekura.DATA, 8);
        AddStarterCard(UneriGinkaku.DATA, 7);
        AddStarterCard(Shichika.DATA, 14);
        AddStarterCard(Togame.DATA, 10);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return HigakiRinne.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return ShikizakiKiki.DATA;
    }
}

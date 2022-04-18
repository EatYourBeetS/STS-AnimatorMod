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
        AddStarterCard(TsurugaMeisai.DATA, 6);
        AddStarterCard(Konayuki.DATA, 8);
        AddStarterCard(ZankiKiguchi.DATA, 5);
        AddStarterCard(UneriGinkaku.DATA, 7);
        AddStarterCard(Azekura.DATA, 9);
        AddStarterCard(Emonzaemon.DATA, 8);

        AddStarterCard(Togame.DATA, 8);
        AddStarterCard(Nanami.DATA, 12);
        AddStarterCard(ManiwaKyouken.DATA, 8);
        AddStarterCard(Shichika.DATA, 15);

        AddStarterCard(HigakiRinne.DATA, 20);
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

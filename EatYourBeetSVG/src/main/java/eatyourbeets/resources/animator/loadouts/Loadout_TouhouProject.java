package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.TouhouProject.*;
import eatyourbeets.cards.animator.ultrarare.YuyukoSaigyouji;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_TouhouProject extends AnimatorLoadout
{
    public Loadout_TouhouProject()
    {
        super(CardSeries.TouhouProject);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(TenshiHinanawi.DATA, 7);
        AddStarterCard(ReimuHakurei.DATA, 8);
        AddStarterCard(MarisaKirisame.DATA, 8);
        AddStarterCard(Rumia.DATA, 12);
        //AddStarterCard(ReisenInaba.DATA, 8);
        //AddStarterCard(ByakurenHijiri.DATA, 5);

        AddStarterCard(YoumuKonpaku.DATA, 13);
        //AddStarterCard(AyaShameimaru.DATA, 15);
        AddStarterCard(SanaeKochiya.DATA, 7);
        AddStarterCard(AliceMargatroid.DATA, 9);
        AddStarterCard(SakuyaIzayoi.DATA, 12);

        AddStarterCard(YukariYakumo.DATA, 20);
        AddStarterCard(HataNoKokoro.DATA, 20);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return ReimuHakurei.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return YuyukoSaigyouji.DATA;
    }
}
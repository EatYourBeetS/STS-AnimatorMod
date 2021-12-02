package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.TouhouProject.*;
import eatyourbeets.cards.animator.beta.ultrarare.YuyukoSaigyouji;
import eatyourbeets.cards.animator.curse.Curse_Clumsy;
import eatyourbeets.cards.animator.curse.Curse_Regret;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_TouhouProject extends AnimatorLoadout
{
    public Loadout_TouhouProject()
    {
        super(CardSeries.TouhouProject);
        IsBeta = true;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(MarisaKirisame.DATA, 4);
        AddStarterCard(ReimuHakurei.DATA, 5);
        AddStarterCard(MayumiJoutouguu.DATA, 5);
        AddStarterCard(YuukaKazami.DATA, 7);
        AddStarterCard(SuikaIbuki.DATA, 10);
        AddStarterCard(Cirno.DATA, 11);
        AddStarterCard(FlandreScarlet.DATA, 15);
        AddStarterCard(Curse_Regret.DATA, -8);
        AddStarterCard(Curse_Clumsy.DATA, -3);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return AyaShameimaru.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return YuyukoSaigyouji.DATA;
    }
}

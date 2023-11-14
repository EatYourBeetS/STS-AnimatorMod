package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.OwariNoSeraph.*;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.animator.ultrarare.HiiragiTenri;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_OwariNoSeraph extends AnimatorLoadout
{
    public Loadout_OwariNoSeraph()
    {
        super(CardSeries.OwariNoSeraph);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Shinoa.DATA, 6);
        AddStarterCard(Shigure.DATA, 6);
        AddStarterCard(Mitsuba.DATA, 7);
        AddStarterCard(KimizugiShiho.DATA, 9);
        AddStarterCard(Yoichi.DATA, 9);
        AddStarterCard(Mikaela.DATA, 8);

        AddStarterCard(HiiragiShinya.DATA, 10);
        AddStarterCard(CrowleyEusford.DATA, 8);
        AddStarterCard(Yuuichirou.DATA, 8);
        AddStarterCard(GoshiNorito.DATA, 8);
        //AddStarterCard(FeridBathory.DATA, 12);

        AddStarterCard(KrulTepes.DATA, 22);
        AddStarterCard(Guren.DATA, 24);

        AddStarterCard(Status_Wound.DATA, -5);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Yuuichirou.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return HiiragiTenri.DATA;
    }
}

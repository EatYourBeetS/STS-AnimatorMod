package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.Curse_Clumsy;
import eatyourbeets.cards.animator.curse.Curse_Parasite;
import eatyourbeets.cards.animator.series.OwariNoSeraph.*;
import eatyourbeets.cards.animator.ultrarare.HiiragiMahiru;
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
        AddStarterCard(Shigure.DATA, 5);
        AddStarterCard(KimizugiShiho.DATA, 6);
        AddStarterCard(Yoichi.DATA, 6);
        AddStarterCard(Mitsuba.DATA, 6);
        AddStarterCard(Shinoa.DATA, 7);
        AddStarterCard(CrowleyEusford.DATA, 8);
        AddStarterCard(Mikaela.DATA, 8);
        AddStarterCard(Yuuichirou.DATA, 8);
        AddStarterCard(Curse_Parasite.DATA, -7);
        AddStarterCard(Curse_Clumsy.DATA, -3);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Yuuichirou.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return HiiragiMahiru.DATA;
    }
}

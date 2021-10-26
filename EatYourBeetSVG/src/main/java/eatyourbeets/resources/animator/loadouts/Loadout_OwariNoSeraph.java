package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.OwariNoSeraph.*;
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
        AddGenericStarters();

        AddStarterCard(Shigure.DATA, 5);
        AddStarterCard(Mitsuba.DATA, 6);
        AddStarterCard(Shinoa.DATA, 7);
        AddStarterCard(CrowleyEusford.DATA, 7);
        AddStarterCard(Mikaela.DATA, 8);
        AddStarterCard(Yuuichirou.DATA, 9);
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

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
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(Mikaela.DATA, 5);
        AddToSpecialSlots(Shigure.DATA, 5);
        AddToSpecialSlots(Mitsuba.DATA, 6);
        AddToSpecialSlots(Shinoa.DATA, 6);
        AddToSpecialSlots(CrowleyEusford.DATA, 7);
        AddToSpecialSlots(Yuuichirou.DATA, 8);
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

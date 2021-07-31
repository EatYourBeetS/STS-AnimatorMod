package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.NoGameNoLife.*;
import eatyourbeets.cards.animator.ultrarare.Azriel;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_NoGameNoLife extends AnimatorLoadout
{
    public Loadout_NoGameNoLife()
    {
        super(CardSeries.NoGameNoLife);
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(DolaCouronne.DATA, 4);
        AddToSpecialSlots(DolaSchwi.DATA, 4);
        AddToSpecialSlots(IzunaHatsuse.DATA, 6);
        AddToSpecialSlots(ChlammyZell.DATA, 7);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Sora.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Azriel.DATA;
    }
}

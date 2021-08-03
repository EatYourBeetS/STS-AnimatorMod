package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.RozenMaiden.*;
import eatyourbeets.cards.animator.beta.ultrarare.Kirakishou;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_RozenMaiden extends AnimatorLoadout
{
    public Loadout_RozenMaiden()
    {
        super(CardSeries.RozenMaiden);
        IsBeta = true;
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(Souseiseki.DATA, 4);
        AddToSpecialSlots(Suiseiseki.DATA, 5);
        AddToSpecialSlots(Hinaichigo.DATA, 5);
        AddToSpecialSlots(JunSakurada.DATA, 7);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Shinku.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Kirakishou.DATA;
    }
}

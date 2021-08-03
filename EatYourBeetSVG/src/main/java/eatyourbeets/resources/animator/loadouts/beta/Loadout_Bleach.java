package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.Bleach.*;
import eatyourbeets.cards.animator.beta.ultrarare.SosukeAizen;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Bleach extends AnimatorLoadout
{
    public Loadout_Bleach()
    {
        super(CardSeries.Bleach);

        IsBeta = true;
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(RenjiAbarai.DATA, 4);
        AddToSpecialSlots(UryuuIshida.DATA, 4);
        AddToSpecialSlots(OrihimeInoue.DATA, 4);
        AddToSpecialSlots(YasutoraSado.DATA, 5);
        AddToSpecialSlots(IchigoKurosaki.DATA, 7);
    }


    @Override
    public EYBCardData GetSymbolicCard()
    {
        return IchigoKurosaki.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SosukeAizen.DATA;
    }
}

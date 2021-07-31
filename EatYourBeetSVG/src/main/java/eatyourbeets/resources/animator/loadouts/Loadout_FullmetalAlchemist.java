package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.FullmetalAlchemist.*;
import eatyourbeets.cards.animator.ultrarare.Truth;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_FullmetalAlchemist extends AnimatorLoadout
{
    public Loadout_FullmetalAlchemist()
    {
        super(CardSeries.FullmetalAlchemist);
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(ElricAlphonse.DATA, 4);
        AddToSpecialSlots(ElricEdward.DATA, 4);
        AddToSpecialSlots(MaesHughes.DATA, 5);
        AddToSpecialSlots(RoyMustang.DATA, 8);
        AddToSpecialSlots(Sloth.DATA, 8);
        AddToSpecialSlots(Pride.DATA, 10);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return RoyMustang.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Truth.DATA;
    }
}

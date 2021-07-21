package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.GoblinSlayer.*;
import eatyourbeets.cards.animator.ultrarare.Hero;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_GoblinSlayer extends AnimatorLoadout
{
    public Loadout_GoblinSlayer()
    {
        super(CardSeries.GoblinSlayer);
    }

    @Override
    public void InitializeSlots()
    {
        super.InitializeSlots();

        AddToSpecialSlots(DwarfShaman.DATA, 4);
        AddToSpecialSlots(LizardPriest.DATA, 4);
        AddToSpecialSlots(HighElfArcher.DATA, 5);
        AddToSpecialSlots(Priestess.DATA, 6);
        AddToSpecialSlots(Spearman.DATA, 7);
        AddToSpecialSlots(Witch.DATA, 9);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return eatyourbeets.cards.animator.series.GoblinSlayer.GoblinSlayer.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Hero.DATA;
    }
}

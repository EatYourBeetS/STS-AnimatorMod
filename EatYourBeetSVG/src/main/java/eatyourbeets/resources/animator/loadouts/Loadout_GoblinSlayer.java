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
    public void AddStarterCards()
    {
        AddStarterCard(DwarfShaman.DATA, 4);
        AddStarterCard(LizardPriest.DATA, 4);
        AddStarterCard(HighElfArcher.DATA, 5);
        AddStarterCard(Priestess.DATA, 6);
        AddStarterCard(Spearman.DATA, 7);
        AddStarterCard(Witch.DATA, 9);
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

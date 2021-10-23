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
        AddGenericStarters();

        AddStarterCard(HighElfArcher.DATA, 5);
        AddStarterCard(DwarfShaman.DATA, 6);
        AddStarterCard(LizardPriest.DATA, 7);
        AddStarterCard(Priestess.DATA, 7);
        AddStarterCard(Spearman.DATA, 8);
        AddStarterCard(Witch.DATA, 8);
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

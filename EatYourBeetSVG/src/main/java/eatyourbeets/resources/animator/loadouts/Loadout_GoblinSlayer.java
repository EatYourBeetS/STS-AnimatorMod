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
        AddStarterCard(Spearman.DATA, 8);
        AddStarterCard(LizardPriest.DATA, 7);
        AddStarterCard(DwarfShaman.DATA, 6);
        AddStarterCard(HighElfArcher.DATA, 6);
        AddStarterCard(Priestess.DATA, 7);

        AddStarterCard(ApprenticeCleric.DATA, 15);
        AddStarterCard(NobleFencer.DATA, 9);
        AddStarterCard(Witch.DATA, 8);
        AddStarterCard(CowGirl.DATA, 8);

        AddStarterCard(HeavyWarrior.DATA, 22);
        AddStarterCard(GoblinSlayer.DATA, 24);
        AddStarterCard(SwordMaiden.DATA, 18);
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

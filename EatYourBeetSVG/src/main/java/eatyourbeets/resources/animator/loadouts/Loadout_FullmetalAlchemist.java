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
    public void AddStarterCards()
    {
        AddGenericStarters();

        AddStarterCard(ElricAlphonse.DATA, 5);
        AddStarterCard(MaesHughes.DATA, 5);
        AddStarterCard(ElricEdward.DATA, 5);
        AddStarterCard(Sloth.DATA, 10);
        AddStarterCard(Pride.DATA, 11);
        AddStarterCard(RoyMustang.DATA, 12);
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

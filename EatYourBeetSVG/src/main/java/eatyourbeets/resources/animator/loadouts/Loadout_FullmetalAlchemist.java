package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.FullmetalAlchemist.*;
import eatyourbeets.cards.animator.status.Crystallize;
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
        AddStarterCard(MaesHughes.DATA, 3);
        AddStarterCard(ElricEdward.DATA, 5);
        AddStarterCard(ElricAlphonse.DATA, 4);
        AddStarterCard(Sloth.DATA, 12);
        AddStarterCard(Lust.DATA, 6);

        AddStarterCard(Pride.DATA, 10);
        AddStarterCard(RoyMustang.DATA, 12);
        AddStarterCard(Scar.DATA, 12);

        AddStarterCard(Father.DATA, 26);
        AddStarterCard(Wrath.DATA, 24);

        AddStarterCard(Crystallize.DATA, -3);
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

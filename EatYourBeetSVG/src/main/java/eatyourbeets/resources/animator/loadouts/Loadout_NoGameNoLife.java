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
    public void AddStarterCards()
    {
        AddStarterCard(DolaCouronne.DATA, 5);
        AddStarterCard(IzunaHatsuse.DATA, 5);
        AddStarterCard(ChlammyZell.DATA, 7);
        AddStarterCard(DolaSchwi.DATA, 9);
        AddStarterCard(Sora.DATA, 16);
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

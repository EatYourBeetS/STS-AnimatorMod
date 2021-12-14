package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.beta.curse.Curse_Slumber;
import eatyourbeets.cards.animator.curse.Curse_Clumsy;
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
        AddStarterCard(DolaStephanie.DATA, 10);
        AddStarterCard(Tet.DATA, 14);
        AddStarterCard(Sora.DATA, 16);
        AddStarterCard(Curse_Clumsy.DATA, -3);
        AddStarterCard(Curse_Slumber.DATA, -7);
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

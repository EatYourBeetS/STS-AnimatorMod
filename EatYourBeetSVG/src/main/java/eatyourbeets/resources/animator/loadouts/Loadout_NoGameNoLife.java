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
        AddStarterCard(DolaSchwi.DATA, 7);
        AddStarterCard(Miko.DATA, 8);
        AddStarterCard(IzunaHatsuse.DATA, 5);
        AddStarterCard(DolaRiku.DATA, 6);
        AddStarterCard(DolaCouronne.DATA, 6);

        AddStarterCard(Tet.DATA, 10);
        AddStarterCard(Jibril.DATA, 11);
        AddStarterCard(DolaStephanie.DATA, 6);
        AddStarterCard(EmirEins.DATA, 11);
        AddStarterCard(ChlammyZell.DATA, 7);

        AddStarterCard(Shiro.DATA, 22);
        AddStarterCard(Sora.DATA, 24);
        AddStarterCard(Holou.DATA, 24);
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

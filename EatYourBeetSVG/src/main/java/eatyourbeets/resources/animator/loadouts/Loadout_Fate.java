package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.Curse_Parasite;
import eatyourbeets.cards.animator.series.Fate.*;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.ultrarare.JeanneDArc;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Fate extends AnimatorLoadout
{
    public Loadout_Fate()
    {
        super(CardSeries.Fate);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(Assassin.DATA, 4);
        AddStarterCard(Alexander.DATA, 6);
        AddStarterCard(Lancer.DATA, 7);
        AddStarterCard(Rider.DATA, 8);
        AddStarterCard(RinTohsaka.DATA, 8);
        AddStarterCard(Berserker.DATA, 10);
        AddStarterCard(EmiyaShirou.DATA, 11);
        AddStarterCard(Saber.DATA, 24);
        AddStarterCard(Crystallize.DATA, -2);
        AddStarterCard(Curse_Parasite.DATA, -7);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Saber.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return JeanneDArc.DATA;
    }
}

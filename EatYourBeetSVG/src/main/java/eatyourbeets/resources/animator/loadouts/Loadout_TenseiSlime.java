package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.TenseiSlime.*;
import eatyourbeets.cards.animator.ultrarare.Veldora;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_TenseiSlime extends AnimatorLoadout
{
    public Loadout_TenseiSlime()
    {
        super(CardSeries.TenseiSlime);
    }

    @Override
    public void AddStarterCards()
    {
        AddGenericStarters();

        AddStarterCard(Benimaru.DATA, 5);
        AddStarterCard(Shuna.DATA, 5);
        AddStarterCard(Shion.DATA, 6);
        AddStarterCard(Souei.DATA, 7);
        AddStarterCard(Hakurou.DATA, 7);
        AddStarterCard(Ranga.DATA, 11);
        AddStarterCard(Rimuru.DATA, 16);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Rimuru.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Veldora.DATA;
    }
}

package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.TenseiSlime.*;
import eatyourbeets.cards.animator.status.Status_Burn;
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
        AddStarterCard(Shion.DATA, 7);
        AddStarterCard(Shuna.DATA, 5);
        AddStarterCard(Hakurou.DATA, 8);
        AddStarterCard(Millim.DATA, 8);
        AddStarterCard(Benimaru.DATA, 6);

        AddStarterCard(Vesta.DATA, 9);
        AddStarterCard(Ranga.DATA, 11);
        AddStarterCard(Souei.DATA, 10);

        AddStarterCard(Rimuru.DATA, 17);
        AddStarterCard(Shizu.DATA, 20);
        AddStarterCard(Ramiris.DATA, 17);

        AddStarterCard(Status_Burn.DATA, -6);
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

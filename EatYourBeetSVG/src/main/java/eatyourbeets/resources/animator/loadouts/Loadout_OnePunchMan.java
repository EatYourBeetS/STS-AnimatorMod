package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.OnePunchMan.*;
import eatyourbeets.cards.animator.ultrarare.SeriousSaitama;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_OnePunchMan extends AnimatorLoadout
{
    public Loadout_OnePunchMan()
    {
        super(CardSeries.OnePunchMan);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(King.DATA, 5);
        AddStarterCard(MumenRider.DATA, 6);
        AddStarterCard(MetalBat.DATA, 5);
        AddStarterCard(Genos.DATA, 6);
        AddStarterCard(SilverFang.DATA, 8);

        AddStarterCard(Sonic.DATA, 10);
        AddStarterCard(DrGenus.DATA, 10);
        AddStarterCard(Tatsumaki.DATA, 15);
        AddStarterCard(Melzalgald.DATA, 12);
        AddStarterCard(MetalKnight.DATA, 15);

        AddStarterCard(Saitama.DATA, 16);
        AddStarterCard(Garou.DATA, 22);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Saitama.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return SeriousSaitama.DATA;
    }
}

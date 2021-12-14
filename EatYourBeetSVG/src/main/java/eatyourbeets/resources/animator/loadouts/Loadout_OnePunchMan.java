package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.beta.curse.Curse_SearingBurn;
import eatyourbeets.cards.animator.curse.Curse_Injury;
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
        AddStarterCard(SilverFang.DATA, 5);
        AddStarterCard(MetalBat.DATA, 5);
        AddStarterCard(MumenRider.DATA, 5);
        AddStarterCard(Genos.DATA, 6);
        AddStarterCard(King.DATA, 7);
        AddStarterCard(Melzalgald.DATA, 12);
        AddStarterCard(Saitama.DATA, 16);
        AddStarterCard(Curse_Injury.DATA, -4);
        AddStarterCard(Curse_SearingBurn.DATA, -6);
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

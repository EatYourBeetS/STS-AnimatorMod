package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.Curse_Clumsy;
import eatyourbeets.cards.animator.curse.Curse_Shame;
import eatyourbeets.cards.animator.series.LogHorizon.*;
import eatyourbeets.cards.animator.ultrarare.Kanami;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_LogHorizon extends AnimatorLoadout
{
    public Loadout_LogHorizon()
    {
        super(CardSeries.LogHorizon);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(HousakiTohya.DATA, 4);
        AddStarterCard(HousakiMinori.DATA, 5);
        AddStarterCard(Serara.DATA, 6);
        AddStarterCard(Marielle.DATA, 7);
        AddStarterCard(Naotsugu.DATA, 8);
        AddStarterCard(RundelhausCode.DATA, 11);
        AddStarterCard(Nyanta.DATA, 11);
        AddStarterCard(Shiroe.DATA, 20);
        AddStarterCard(Curse_Clumsy.DATA, -3);
        AddStarterCard(Curse_Shame.DATA, -7);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Akatsuki.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Kanami.DATA;
    }
}

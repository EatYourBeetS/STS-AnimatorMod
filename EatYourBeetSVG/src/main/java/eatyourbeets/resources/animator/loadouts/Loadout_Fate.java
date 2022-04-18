package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.Fate.*;
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
        AddStarterCard(Berserker.DATA, 10);
        AddStarterCard(Assassin.DATA, 4);
        AddStarterCard(EmiyaShirou.DATA, 11);
        AddStarterCard(Alexander.DATA, 6);
        AddStarterCard(MatouShinji.DATA, 8);
        AddStarterCard(Rider.DATA, 8);

        AddStarterCard(Illya.DATA, 11);
        AddStarterCard(RinTohsaka.DATA, 6);
        AddStarterCard(MatouSakura.DATA, 12);
        AddStarterCard(Lancer.DATA, 7);
        AddStarterCard(Caster.DATA, 14);

        AddStarterCard(Saber.DATA, 24);
        AddStarterCard(EmiyaKiritsugu.DATA, 11);
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

package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.curse.Curse_Writhe;
import eatyourbeets.cards.animator.series.MadokaMagica.*;
import eatyourbeets.cards.animator.ultrarare.Walpurgisnacht;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_MadokaMagica extends AnimatorLoadout
{
    public Loadout_MadokaMagica()
    {
        super(CardSeries.MadokaMagica);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(IrohaTamaki.DATA, 5);
        AddStarterCard(FeliciaMitsuki.DATA, 4);
        AddStarterCard(OrikoMikuni.DATA, 4);
        AddStarterCard(KyokoSakura.DATA, 7);
        AddStarterCard(YuiTsuruno.DATA, 7);
        AddStarterCard(NagisaMomoe.DATA, 10);
        AddStarterCard(SayakaMiki.DATA, 10);
        AddStarterCard(MadokaKaname.DATA, 25);
        AddStarterCard(Curse_GriefSeed.DATA, -2);
        AddStarterCard(Curse_Writhe.DATA, -5);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return MadokaKaname.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return Walpurgisnacht.DATA;
    }
}

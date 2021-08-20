package eatyourbeets.resources.animator.loadouts;

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
        AddStarterCard(IrohaTamaki.DATA, 4);
        AddStarterCard(OrikoMikuni.DATA, 4);
        AddStarterCard(YuiTsuruno.DATA, 5);
        AddStarterCard(NagisaMomoe.DATA, 5);
        AddStarterCard(KyokoSakura.DATA, 6);
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

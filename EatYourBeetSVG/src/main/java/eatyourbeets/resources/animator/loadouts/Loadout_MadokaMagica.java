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
        AddGenericStarters();

        AddStarterCard(IrohaTamaki.DATA, 5);
        AddStarterCard(OrikoMikuni.DATA, 5);
        AddStarterCard(NagisaMomoe.DATA, 5);
        AddStarterCard(KyokoSakura.DATA, 7);
        AddStarterCard(YuiTsuruno.DATA, 8);
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

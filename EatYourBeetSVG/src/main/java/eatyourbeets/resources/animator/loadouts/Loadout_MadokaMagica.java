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
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(IrohaTamaki.DATA, 4);
        AddToSpecialSlots(OrikoMikuni.DATA, 4);
        AddToSpecialSlots(YuiTsuruno.DATA, 5);
        AddToSpecialSlots(NagisaMomoe.DATA, 5);
        AddToSpecialSlots(KyokoSakura.DATA, 6);
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

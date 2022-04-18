package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.MadokaMagica.*;
import eatyourbeets.cards.animator.ultrarare.Walpurgisnacht;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_MadokaMagica extends AnimatorLoadout
{
    private static final EYBCardTooltip witchTooltip = new EYBCardTooltip("", "");

    public Loadout_MadokaMagica()
    {
        super(CardSeries.MadokaMagica);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(SanaFutaba.DATA, 8);
        AddStarterCard(IrohaTamaki.DATA, 6);
        AddStarterCard(YuiTsuruno.DATA, 10);
        AddStarterCard(FeliciaMitsuki.DATA, 8);
        AddStarterCard(MamiTomoe.DATA, 7);
        AddStarterCard(KyokoSakura.DATA, 9);

        AddStarterCard(NagisaMomoe.DATA, 10);
        AddStarterCard(SayakaMiki.DATA, 9);
        AddStarterCard(OrikoMikuni.DATA, 10);
        AddStarterCard(SuzuneAmano.DATA, 9);

        AddStarterCard(HomuraAkemi.DATA, 17);
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

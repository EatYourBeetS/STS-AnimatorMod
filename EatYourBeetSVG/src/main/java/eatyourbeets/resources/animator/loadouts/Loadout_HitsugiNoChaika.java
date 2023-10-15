package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.HitsugiNoChaika.*;
import eatyourbeets.cards.animator.ultrarare.NivaLada;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_HitsugiNoChaika extends AnimatorLoadout
{
    private static final EYBCardTooltip tooruTooltip = new EYBCardTooltip("", "");

    public Loadout_HitsugiNoChaika()
    {
        super(CardSeries.HitsugiNoChaika);
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(MattheusCallaway.DATA, 6);
        AddStarterCard(ZitaBrusasco.DATA, 6);
        AddStarterCard(Viivi.DATA, 8);
        AddStarterCard(Gillette.DATA, 6);
        AddStarterCard(Nikolay.DATA, 7);
        AddStarterCard(ChaikaBohdan.DATA, 10);

        AddStarterCard(AcuraTooru.DATA, 10);
        //AddStarterCard(Fredrika.DATA, 14);
        AddStarterCard(Layla.DATA, 10);
        AddStarterCard(ClaudiaDodge.DATA, 8);
        AddStarterCard(AcuraAkari.DATA, 7);
        AddStarterCard(ChaikaGaz.DATA, 4);

        AddStarterCard(ChaikaTrabant.DATA, 20);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return ChaikaTrabant.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return NivaLada.DATA;
    }
}

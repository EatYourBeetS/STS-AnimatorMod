package eatyourbeets.resources.animator.loadouts;

import eatyourbeets.cards.animator.series.Katanagatari.*;
import eatyourbeets.cards.animator.ultrarare.ShikizakiKiki;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_Katanagatari extends AnimatorLoadout
{
    public Loadout_Katanagatari()
    {
        super(CardSeries.Katanagatari);
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(Azekura.DATA, 5);
        AddToSpecialSlots(Emonzaemon.DATA, 5);
        AddToSpecialSlots(UneriGinkaku.DATA, 7);
        AddToSpecialSlots(Shichika.DATA, 8);
        AddToSpecialSlots(Togame.DATA, 8);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return HigakiRinne.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return ShikizakiKiki.DATA;
    }
}

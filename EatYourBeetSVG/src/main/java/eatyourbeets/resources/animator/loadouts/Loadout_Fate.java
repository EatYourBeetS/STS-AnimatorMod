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
    public void InitializeSlots()
    {
        super.InitializeSlots();

        AddToSpecialSlots(Assassin.DATA, 4);
        AddToSpecialSlots(Alexander.DATA, 5);
        AddToSpecialSlots(RinTohsaka.DATA, 5);
        AddToSpecialSlots(Lancer.DATA, 6);
        AddToSpecialSlots(Berserker.DATA, 9);
        AddToSpecialSlots(EmiyaShirou.DATA, 11);
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

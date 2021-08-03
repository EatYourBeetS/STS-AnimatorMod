package eatyourbeets.resources.animator.loadouts.beta;

import eatyourbeets.cards.animator.beta.series.TouhouProject.*;
import eatyourbeets.cards.animator.beta.ultrarare.YuyukoSaigyouji;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;

public class Loadout_TouhouProject extends AnimatorLoadout
{
    public Loadout_TouhouProject()
    {
        super(CardSeries.TouhouProject);
        IsBeta = true;
    }

    @Override
    public void InitializeData()
    {
        super.InitializeData();

        AddToSpecialSlots(MarisaKirisame.DATA, 4);
        AddToSpecialSlots(ReimuHakurei.DATA, 5);
        AddToSpecialSlots(MayumiJoutouguu.DATA, 5);
        AddToSpecialSlots(Clownpiece.DATA, 9);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return AyaShameimaru.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return YuyukoSaigyouji.DATA;
    }
}

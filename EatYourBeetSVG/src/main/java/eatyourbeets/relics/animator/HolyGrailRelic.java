package eatyourbeets.relics.animator;

import eatyourbeets.cards.animator.ultrarare.HolyGrail;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class HolyGrailRelic extends AnimatorRelic
{
    public static final String ID = CreateFullID(HolyGrailRelic.class.getSimpleName());

    public HolyGrailRelic()
    {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        GameActions.Bottom.MakeCardInHand(new HolyGrail());
        this.flash();
    }
}
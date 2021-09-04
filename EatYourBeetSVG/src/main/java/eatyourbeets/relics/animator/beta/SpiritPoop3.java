package eatyourbeets.relics.animator.beta;

import eatyourbeets.powers.common.InspirationPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class SpiritPoop3 extends AnimatorRelic
{
    public static final String ID = CreateFullID(SpiritPoop3.class);

    public SpiritPoop3()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();
        GameActions.Bottom.StackPower(new InspirationPower(player, 2));
    }
}
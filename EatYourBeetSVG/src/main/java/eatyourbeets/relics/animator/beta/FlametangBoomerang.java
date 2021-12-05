package eatyourbeets.relics.animator.beta;

import eatyourbeets.powers.animator.BurningWeaponPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class FlametangBoomerang extends AnimatorRelic
{
    public static final String ID = CreateFullID(FlametangBoomerang.class);

    public FlametangBoomerang()
    {
        super(ID, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();
        GameActions.Bottom.StackPower(new BurningWeaponPower(player , 2));
    }
}
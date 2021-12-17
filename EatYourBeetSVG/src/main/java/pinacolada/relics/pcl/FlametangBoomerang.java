package pinacolada.relics.pcl;

import pinacolada.powers.special.BurningWeaponPower;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

public class FlametangBoomerang extends PCLRelic
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
        PCLActions.Bottom.StackPower(new BurningWeaponPower(player , 2));
    }
}
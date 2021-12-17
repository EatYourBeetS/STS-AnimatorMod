package pinacolada.relics.pcl;

import pinacolada.powers.common.InspirationPower;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

public class SpiritPoop3 extends PCLRelic
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
        PCLActions.Bottom.StackPower(new InspirationPower(player, 2));
    }
}
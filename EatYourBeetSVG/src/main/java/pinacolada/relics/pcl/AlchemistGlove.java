package pinacolada.relics.pcl;

import pinacolada.orbs.pcl.Fire;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

public class AlchemistGlove extends PCLRelic
{
    public static final String ID = CreateFullID(AlchemistGlove.class);

    public AlchemistGlove()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLActions.Bottom.ChannelOrb(new Fire());
    }
}
package pinacolada.relics.pcl.replacement;

import pinacolada.orbs.pcl.Fire;
import pinacolada.relics.PCLReplacementRelic;
import pinacolada.utilities.PCLActions;

public class AlchemistGlove extends PCLReplacementRelic
{
    public static final String ID = CreateFullID(AlchemistGlove.class);

    public AlchemistGlove()
    {
        super(ID, eatyourbeets.relics.animator.AlchemistGlove.ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PCLActions.Bottom.ChannelOrb(new Fire());
    }
}
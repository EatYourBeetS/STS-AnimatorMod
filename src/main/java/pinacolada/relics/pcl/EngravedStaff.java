package pinacolada.relics.pcl;

import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

public class EngravedStaff extends PCLRelic
{
    public static final String ID = CreateFullID(EngravedStaff.class);

    public EngravedStaff()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        PCLActions.Bottom.Callback(() ->
        {
            PCLActions.Bottom.GainRandomAffinityPower(1, true);
            flash();
        });
    }
}
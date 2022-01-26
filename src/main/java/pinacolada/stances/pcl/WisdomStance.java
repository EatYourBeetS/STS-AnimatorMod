package pinacolada.stances.pcl;

import com.badlogic.gdx.graphics.Color;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.affinity.WisdomPower;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLGameUtilities;

public class WisdomStance extends PCLStance
{
    public static final PCLAffinity AFFINITY = WisdomPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(WisdomStance.class);

    public static boolean IsActive()
    {
        return PCLGameUtilities.InStance(STANCE_ID);
    }

    public WisdomStance()
    {
        super(PCLStanceHelper.WisdomStance);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.3f, 0.4f, 0.8f, 0.9f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.2f, 0.3f, 0.2f, 0.3f, 0.8f, 0.9f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 0.25f, 1f, 1f);
    }

}

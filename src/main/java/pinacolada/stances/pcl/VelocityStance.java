package pinacolada.stances.pcl;

import com.badlogic.gdx.graphics.Color;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.affinity.VelocityPower;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLGameUtilities;

public class VelocityStance extends PCLStance
{
    public static final PCLAffinity AFFINITY = VelocityPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(VelocityStance.class);

    public static boolean IsActive()
    {
        return PCLGameUtilities.InStance(STANCE_ID);
    }

    public VelocityStance()
    {
        super(PCLStanceHelper.VelocityStance);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.7f, 0.8f, 0.2f, 0.3f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.4f, 0.5f, 0.8f, 0.9f, 0.4f, 0.5f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 1f, 0.2f, 1f);
    }
}

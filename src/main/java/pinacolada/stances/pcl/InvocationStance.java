package pinacolada.stances.pcl;

import com.badlogic.gdx.graphics.Color;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.affinity.InvocationPower;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLGameUtilities;

public class InvocationStance extends PCLStance
{
    public static final PCLAffinity AFFINITY = InvocationPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(InvocationStance.class);

    public static boolean IsActive()
    {
        return PCLGameUtilities.InStance(STANCE_ID);
    }

    public InvocationStance()
    {
        super(PCLStanceHelper.InvocationStance);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.8f, 1f, 0.8f, 1f, 0.3f, 0.4f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.8f, 1f, 0.8f, 1f, 0.3f, 0.4f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(1f, 1f, 0.3f, 1f);
    }
}

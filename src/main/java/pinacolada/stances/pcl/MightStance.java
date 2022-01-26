package pinacolada.stances.pcl;

import com.badlogic.gdx.graphics.Color;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.affinity.MightPower;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLGameUtilities;

public class MightStance extends PCLStance
{
    public static final PCLAffinity AFFINITY = MightPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(MightStance.class);

    public static boolean IsActive()
    {
        return PCLGameUtilities.InStance(STANCE_ID);
    }

    public MightStance()
    {
        super(PCLStanceHelper.MightStance);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.8f, 0.9f, 0.3f, 0.4f, 0.2f, 0.3f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.8f, 0.9f, 0.3f, 0.4f, 0.2f, 0.3f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(1f, 0.3f, 0.2f, 1f);
    }
}

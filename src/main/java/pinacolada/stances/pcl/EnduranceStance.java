package pinacolada.stances.pcl;

import com.badlogic.gdx.graphics.Color;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.affinity.EndurancePower;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLGameUtilities;

public class EnduranceStance extends PCLStance
{
    public static final PCLAffinity AFFINITY = EndurancePower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(EnduranceStance.class);

    public static boolean IsActive()
    {
        return PCLGameUtilities.InStance(STANCE_ID);
    }

    public EnduranceStance()
    {
        super(PCLStanceHelper.EnduranceStance);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.8f, 0.9f, 0.5f, 0.6f, 0.2f, 0.3f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.8f, 0.9f, 0.5f, 0.6f, 0.2f, 0.3f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(1f, 0.6f, 0.2f, 1f);
    }
}

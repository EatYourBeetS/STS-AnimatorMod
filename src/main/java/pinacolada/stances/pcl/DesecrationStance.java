package pinacolada.stances.pcl;

import com.badlogic.gdx.graphics.Color;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.affinity.DesecrationPower;
import pinacolada.stances.PCLStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLGameUtilities;

public class DesecrationStance extends PCLStance
{
    public static final PCLAffinity AFFINITY = DesecrationPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(DesecrationStance.class);

    public static boolean IsActive()
    {
        return PCLGameUtilities.InStance(STANCE_ID);
    }

    public DesecrationStance()
    {
        super(PCLStanceHelper.DesecrationStance);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.3f, 0.35f, 0.2f, 0.3f, 0.4f, 0.5f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.3f, 0.35f, 0.1f, 0.2f, 0.4f, 0.5f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.3f, 0.2f, 0.7f, 1f);
    }

}

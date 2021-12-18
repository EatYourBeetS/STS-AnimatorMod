package pinacolada.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.affinity.WisdomPower;
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
        super(STANCE_ID, AFFINITY, AbstractDungeon.player);
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

package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.affinity.DesecrationPower;
import eatyourbeets.utilities.GameUtilities;

public class DesecrationStance extends EYBStance
{
    public static final Affinity AFFINITY = DesecrationPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(DesecrationStance.class);

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public DesecrationStance()
    {
        super(STANCE_ID, AFFINITY, AbstractDungeon.player);
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

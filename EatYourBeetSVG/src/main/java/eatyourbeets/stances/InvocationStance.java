package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.affinity.InvocationPower;
import eatyourbeets.utilities.GameUtilities;

public class InvocationStance extends EYBStance
{
    public static final Affinity AFFINITY = InvocationPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(InvocationStance.class);

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public InvocationStance()
    {
        super(STANCE_ID, AFFINITY, AbstractDungeon.player);
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

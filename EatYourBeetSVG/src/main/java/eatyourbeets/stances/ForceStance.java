package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.affinity.FireLevelPower;
import eatyourbeets.utilities.GameUtilities;

public class ForceStance extends EYBStance
{
    public static final Affinity AFFINITY = FireLevelPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(ForceStance.class);

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public ForceStance()
    {
        super(STANCE_ID, AFFINITY, AbstractDungeon.player);
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

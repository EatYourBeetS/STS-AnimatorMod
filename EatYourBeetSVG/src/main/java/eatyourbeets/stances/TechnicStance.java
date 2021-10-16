package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.affinity.TechnicPower;
import eatyourbeets.utilities.GameUtilities;

public class TechnicStance extends EYBStance
{
    public static final Affinity AFFINITY = TechnicPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(TechnicStance.class);

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public TechnicStance()
    {
        super(STANCE_ID, AFFINITY, AbstractDungeon.player);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.5f, 0.5f, 0.5f, 0.6f, 0.6f, 0.6f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.7f, 0.7f, 0.7f, 0.3f, 0.3f, 0.3f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.55f, 0.55f, 0.55f, 1f);
    }
}

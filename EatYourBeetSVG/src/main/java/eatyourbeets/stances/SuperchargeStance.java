package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.affinity.SuperchargePower;
import eatyourbeets.utilities.GameUtilities;

public class SuperchargeStance extends EYBStance
{
    public static final Affinity AFFINITY = SuperchargePower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(SuperchargeStance.class);

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public SuperchargeStance()
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

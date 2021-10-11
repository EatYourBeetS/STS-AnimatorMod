package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameUtilities;

public class OrbStance extends EYBStance
{
    public static final String STANCE_ID = CreateFullID(OrbStance.class);
    private static final int STAT_GAIN_AMOUNT = 3;
    private static final int STAT_LOSE_AMOUNT = 3;

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

    public OrbStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    @Override
    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.2f, 0.3f, 0.8f, 0.9f);
    }

    @Override
    protected Color GetAuraColor()
    {
        return CreateColor(0.2f, 0.3f, 0.1f, 0.2f, 0.6f, 0.7f);
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 0.2f, 1f, 1f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        if (TryApplyStance(STANCE_ID))
        {
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus, +STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, -STAT_LOSE_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, -STAT_LOSE_AMOUNT);
        }
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        if (TryApplyStance(null))
        {
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus, -STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, +STAT_LOSE_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, +STAT_LOSE_AMOUNT);
        }
    }
}

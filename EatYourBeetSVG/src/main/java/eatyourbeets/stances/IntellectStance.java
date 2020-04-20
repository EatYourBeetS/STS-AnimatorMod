package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class IntellectStance extends EYBStance
{
    public static String STANCE_ID = CreateFullID(IntellectStance.class);

    public IntellectStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.2f, 0.3f, 0.8f, 0.9f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.2f, 0.3f, 0.1f, 0.2f, 0.6f, 0.7f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        GameActions.Bottom.GainIntellect(1, true);

        ApplyPowerSilently(PowerHelper.Focus    , -2);
        ApplyPowerSilently(PowerHelper.Dexterity,  1);
        ApplyPowerSilently(PowerHelper.Strength ,  1);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        ApplyPowerSilently(PowerHelper.Focus    ,  2);
        ApplyPowerSilently(PowerHelper.Dexterity, -1);
        ApplyPowerSilently(PowerHelper.Strength , -1);
    }

    @Override
    protected void QueueParticle()
    {
        GameEffects.Queue.Add(new StanceParticleVertical(GetParticleColor()));
    }

    @Override
    protected void QueueAura()
    {
        GameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 0.2f, 1f, 1f);
    }
}
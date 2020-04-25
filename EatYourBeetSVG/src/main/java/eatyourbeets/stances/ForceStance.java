package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ForceStance extends EYBStance
{
    public static String STANCE_ID = CreateFullID(ForceStance.class);

    public ForceStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.8f, 0.9f, 0.3f, 0.4f, 0.2f, 0.3f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.8f, 0.9f, 0.3f, 0.4f, 0.2f, 0.3f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        GameActions.Bottom.GainForce(1, true);

        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus    , -1);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, -1);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength , +2);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        GameActions.Bottom.GainTemporaryHP(3);

        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus    , +1);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, +1);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength , -2);
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
        return new Color(1f, 0.3f, 0.2f, 1f);
    }
}
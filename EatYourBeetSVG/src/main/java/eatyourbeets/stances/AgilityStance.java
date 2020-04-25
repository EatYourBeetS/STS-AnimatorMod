package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.effects.stance.StanceParticleHorizontal;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class AgilityStance extends EYBStance
{
    public static final String STANCE_ID = CreateFullID(AgilityStance.class);

    public AgilityStance()
    {
        super(STANCE_ID, AbstractDungeon.player);
    }

    protected Color GetParticleColor()
    {
        return CreateColor(0.2f, 0.3f, 0.7f, 0.8f, 0.2f, 0.3f);
    }

    protected Color GetAuraColor()
    {
        return CreateColor(0.4f, 0.5f, 0.8f, 0.9f, 0.4f, 0.5f);
    }

    @Override
    public void onEnterStance()
    {
        super.onEnterStance();

        GameActions.Bottom.GainAgility(1, true);

        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus    , -1);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, +2);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength , -1);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        GameActions.Bottom.ApplyPower(new DrawCardNextTurnPower(AbstractDungeon.player, 1));

        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus    , +1);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, -2);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength , +1);
    }

    @Override
    protected void QueueParticle()
    {
        GameEffects.Queue.Add(new StanceParticleHorizontal(GetParticleColor()));
        GameEffects.Queue.Add(new StanceParticleVertical(GetAuraColor()));
    }

    @Override
    protected void QueueAura()
    {
        //GameEffects.Queue.Add(new StanceAura(GetAuraColor()));
    }

    @Override
    protected Color GetMainColor()
    {
        return new Color(0.2f, 1f, 0.2f, 1f);
    }
}
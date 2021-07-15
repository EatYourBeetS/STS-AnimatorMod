package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.effects.stance.StanceParticleHorizontal;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class AgilityStance extends EYBStance
{
    public static final AffinityType AFFINITY = AgilityPower.AFFINITY_TYPE;
    public static final String STANCE_ID = CreateFullID(AgilityStance.class);
    public static final int STAT_GAIN_AMOUNT = 2;
    public static final int STAT_LOSE_AMOUNT = 1;
    public static final int DRAW_AMOUNT = 1;

    public static boolean IsActive()
    {
        return GameUtilities.InStance(STANCE_ID);
    }

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

        GameActions.Bottom.StackAffinityPower(AFFINITY, 1, true);

        if (TryApplyStance(STANCE_ID))
        {
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus, -STAT_LOSE_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, +STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, -STAT_LOSE_AMOUNT);
        }
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        if (TryApplyStance(null))
        {
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus, +STAT_LOSE_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, -STAT_GAIN_AMOUNT);
            GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength, +STAT_LOSE_AMOUNT);
        }
    }

    @Override
    public void onRefreshStance()
    {
        GameUtilities.RetainPower(AFFINITY);
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

    @Override
    public void updateDescription()
    {
        description = FormatDescription(STAT_GAIN_AMOUNT, STAT_LOSE_AMOUNT, DRAW_AMOUNT);
    }
}

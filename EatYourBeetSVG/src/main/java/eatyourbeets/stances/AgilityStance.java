package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.stance.StanceParticleHorizontal;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class AgilityStance extends EYBStance
{
    public static final Affinity AFFINITY = AgilityPower.AFFINITY_TYPE;
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
        super(STANCE_ID, AFFINITY, AbstractDungeon.player);
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
            CombatStats.Affinities.GetPower(Affinity.Red).AddThresholdBonusModifier(-STAT_LOSE_AMOUNT);
            CombatStats.Affinities.GetPower(Affinity.Blue).AddThresholdBonusModifier(-STAT_LOSE_AMOUNT);
            CombatStats.Affinities.GetPower(Affinity.Green).AddThresholdBonusModifier(+STAT_GAIN_AMOUNT);
        }
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        if (TryApplyStance(null))
        {
            CombatStats.Affinities.GetPower(Affinity.Red).AddThresholdBonusModifier(+STAT_LOSE_AMOUNT);
            CombatStats.Affinities.GetPower(Affinity.Blue).AddThresholdBonusModifier(+STAT_LOSE_AMOUNT);
            CombatStats.Affinities.GetPower(Affinity.Green).AddThresholdBonusModifier(-STAT_GAIN_AMOUNT);
        }
    }

    @Override
    public void onRefreshStance()
    {
        GameActions.Bottom.StackAffinityPower(AFFINITY, 1, true);
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

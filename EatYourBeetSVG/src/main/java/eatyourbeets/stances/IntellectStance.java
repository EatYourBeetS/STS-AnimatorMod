package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class IntellectStance extends EYBStance
{
    public static final String STANCE_ID = CreateFullID(IntellectStance.class);
    public static final int STAT_GAIN_AMOUNT = 2;
    public static final int STAT_LOSE_AMOUNT = 1;
    public static final int DAMAGE_AMOUNT = 7;

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
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus    , +STAT_GAIN_AMOUNT);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, -STAT_LOSE_AMOUNT);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength , -STAT_LOSE_AMOUNT);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        GameActions.Bottom.DealDamageToRandomEnemy(DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Focus    , -STAT_GAIN_AMOUNT);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Dexterity, +STAT_LOSE_AMOUNT);
        GameUtilities.ApplyPowerInstantly(owner, PowerHelper.Strength , +STAT_LOSE_AMOUNT);
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

    @Override
    public void updateDescription()
    {
        description = FormatDescription(STAT_GAIN_AMOUNT, STAT_LOSE_AMOUNT, DAMAGE_AMOUNT);
    }
}

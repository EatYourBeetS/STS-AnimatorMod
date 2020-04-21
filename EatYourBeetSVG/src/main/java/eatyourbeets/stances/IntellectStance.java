package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.powers.common.DamageNextTurnPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class IntellectStance extends EYBStance
{
    public static String STANCE_ID = CreateFullID(IntellectStance.class);
    public static int STAT_GAIN_AMOUNT = 2;
    public static int STAT_LOSE_AMOUNT = 1;
    public static int DAMAGE_AMOUNT = 4;

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

        GameActions.Bottom.GainIntellect(1);
        GameActions.Bottom.GainFocus(STAT_GAIN_AMOUNT);
        GameActions.Bottom.GainStrength(-STAT_LOSE_AMOUNT)
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
        GameActions.Bottom.GainDexterity(-STAT_LOSE_AMOUNT)
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        GameActions.Bottom.ApplyPower(new DamageNextTurnPower(AbstractDungeon.player, DAMAGE_AMOUNT));
        GameActions.Bottom.GainFocus(-STAT_GAIN_AMOUNT)
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
        GameActions.Bottom.GainStrength(STAT_LOSE_AMOUNT);
        GameActions.Bottom.GainDexterity(STAT_LOSE_AMOUNT);
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

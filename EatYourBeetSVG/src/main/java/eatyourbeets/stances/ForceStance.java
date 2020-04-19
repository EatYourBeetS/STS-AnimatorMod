package eatyourbeets.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.effects.stance.StanceAura;
import eatyourbeets.effects.stance.StanceParticleVertical;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class ForceStance extends EYBStance
{
    public static String STANCE_ID = CreateFullID(ForceStance.class);
    public static int STAT_GAIN_AMOUNT = 2;
    public static int ENEMY_DAMAGE_BONUS_PERCENTAGE = 25;

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

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainStrength(STAT_GAIN_AMOUNT);
    }

    @Override
    public void onExitStance()
    {
        super.onExitStance();

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.GainStrength(-STAT_GAIN_AMOUNT)
        .ShowEffect(false, true)
        .IgnoreArtifact(true);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        if (damageType == DamageInfo.DamageType.NORMAL)
        {
            float enemyDamageBonus = (float) ENEMY_DAMAGE_BONUS_PERCENTAGE / 100f;

            return damage * (1f + enemyDamageBonus);
        }

        return damage;
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

    @Override
    public void updateDescription()
    {
        description = FormatDescription(STAT_GAIN_AMOUNT, ENEMY_DAMAGE_BONUS_PERCENTAGE);
    }
}

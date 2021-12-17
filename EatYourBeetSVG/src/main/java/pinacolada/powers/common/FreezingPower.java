package pinacolada.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLTriggerablePower;

public class FreezingPower extends PCLTriggerablePower
{
    private static final Color healthBarColor = new Color(0.372F, 0.5F, 1.0F, 1.0F);
    public static final String POWER_ID = CreateFullID(FreezingPower.class);
    public static final int REDUCTION_MULTIPLIER = 10;
    public static final int MAX_REDUCTION_MULTIPLIER = 50;

    public static float CalculateDamage(float damage, float multiplier)
    {
        return Math.max(0, damage - Math.max(1f, damage * (multiplier / 100f)));
    }

    public FreezingPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID, REDUCTION_MULTIPLIER, MAX_REDUCTION_MULTIPLIER);

        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ORB_FROST_CHANNEL, 0.95f, 1.05f);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageGive((type == DamageInfo.DamageType.NORMAL) ? CalculateDamage(damage, GetEffectMultiplier()) : damage, type);
    }

    @Override
    public AbstractGameAction.AttackEffect GetAttackEffect() {
        return AttackEffects.ICE;
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }
}

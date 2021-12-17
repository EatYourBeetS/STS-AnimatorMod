package pinacolada.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLTriggerablePower;

public class BurningPower extends PCLTriggerablePower
{
    private static final Color healthBarColor = Color.ORANGE.cpy();
    public static final String POWER_ID = CreateFullID(BurningPower.class);
    public static final int ATTACK_MULTIPLIER = 15;

    public static float CalculateDamage(float damage, float multiplier)
    {
        return damage + Mathf.Max(1, damage * (multiplier / 100f));
    }

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID, ATTACK_MULTIPLIER);

        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ATTACK_FIRE, 0.95f, 1.05f);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageReceive(type == DamageInfo.DamageType.NORMAL ? CalculateDamage(damage, GetEffectMultiplier()) : damage, type);
    }

    @Override
    public AbstractGameAction.AttackEffect GetAttackEffect() {
        return AttackEffects.FIRE;
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }
}
package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.Mathf;

public class BurningPower extends CommonPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.ORANGE.cpy();

    public static final String POWER_ID = CreateFullID(BurningPower.class);
    public static final int ATTACK_MULTIPLIER = 15;

    private final AbstractCreature source;

    public static float CalculateDamage(float damage)
    {
        return damage + Mathf.Max(1, damage * (ATTACK_MULTIPLIER / 100f));
    }

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, POWER_ID);

        this.source = source;
        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, GetPassiveDamage(), ATTACK_MULTIPLIER);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ATTACK_FIRE, 0.95f, 1.05f);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        GameActions.Bottom.DealDamage(source, owner, GetPassiveDamage(), DamageInfo.DamageType.HP_LOSS, AttackEffects.FIRE);
        ReducePower(1);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        return super.atDamageReceive(type == DamageInfo.DamageType.NORMAL ? CalculateDamage(damage) : damage, type);
    }

    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, GetPassiveDamage(), false, true);
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new BurningPower(owner, source, amount);
    }

    private int GetPassiveDamage()
    {
        return amount == 1 ? 1 : amount < 1 ? 0 : amount / 2 + amount % 2;
    }
}

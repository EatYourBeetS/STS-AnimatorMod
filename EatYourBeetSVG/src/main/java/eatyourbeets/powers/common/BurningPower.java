package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.Mathf;

public class BurningPower extends CommonPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.ORANGE.cpy();

    public static final String POWER_ID = CreateFullID(BurningPower.class);
    public static final int BASE_MULTIPLIER = 10;
    public static final int MAX_MULTIPLIER_STACKS = 20;
    public static final float RATE = 1.0f;

    private float percentage;

    public static float CalculatePercentage(int amount) {return BASE_MULTIPLIER + RATE * Math.min(MAX_MULTIPLIER_STACKS,amount);}
    public static float CalculateDamage(float damage, float percentage)
    {
        return damage + Mathf.Max(1, damage * (percentage / 100f));
    }

    public BurningPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID);

        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);

        updatePercentage();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, GetPassiveDamage(), percentage);
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
        return super.atDamageReceive(type == DamageInfo.DamageType.NORMAL ? CalculateDamage(damage, percentage) : damage, type);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        updatePercentage();
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        updatePercentage();
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

    private void updatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
        updateDescription();
    }
}

package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;

public class FreezingPower extends CommonPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = new Color(0.372F, 0.5F, 1.0F, 1.0F);
    public static final String POWER_ID = CreateFullID(FreezingPower.class);
    public static final int BASE_MULTIPLIER = 10;
    public static final int MAX_MULTIPLIER_STACKS = 20;
    public static final float RATE = 0.5f;

    private float percentage;
    private final AbstractCreature source;

    public static float CalculatePercentage(int amount) {return BASE_MULTIPLIER + RATE * Math.min(MAX_MULTIPLIER_STACKS,amount);}
    public static float CalculateDamage(float damage, float percentage)
    {
        return damage - MathUtils.ceil( damage * (percentage / 100f));
    }

    public FreezingPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, POWER_ID);

        this.source = source == null ? owner : source;
        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

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
        CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        GameActions.Bottom.DealDamage(source, owner, GetPassiveDamage(), DamageInfo.DamageType.HP_LOSS, AttackEffects.ICE);
        ReducePower(1);
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
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        float newDamage = calculateDamageGiven(damage, type);
        return super.atDamageGive(newDamage, type);
    }

    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, amount, false, true);
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    public int calculateDamageGiven(float damage, DamageInfo.DamageType type)
    {
        return (int) ((type == DamageInfo.DamageType.NORMAL) ? CalculateDamage(damage, percentage) : damage);
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

package eatyourbeets.powers.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class ChilledPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ChilledPower.class);

    private float percentage;

    public static float CalculatePercentage(int amount)
    {
        return 100f / (125f + (2.1f * amount));
    }

    public ChilledPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

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
        if (amount > 0)
        {
            DecimalFormat df = new DecimalFormat("#.0");
            String value = df.format(((1 - this.percentage) * 100));

            this.description = powerStrings.DESCRIPTIONS[0] + value + powerStrings.DESCRIPTIONS[1];
        }
    }

    @Override
    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f);
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
    public void atEndOfRound()
    {
        super.atEndOfRound();
        GameActions.Bottom.ReducePower(this, Math.max(MathUtils.ceil(this.amount * 0.75f),1));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        float newDamage = calculateDamageGiven(damage, type);
        return super.atDamageGive(newDamage, type);
    }

    public int calculateDamageGiven(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            float multiplier = (owner.hasPower(WeakPower.POWER_ID)) ? CalculatePercentage(this.amount / 2) : percentage;
            return MathUtils.ceil(multiplier * damage);
        }
        return (int) damage;
    }

    private void updatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
        updateDescription();
    }
}

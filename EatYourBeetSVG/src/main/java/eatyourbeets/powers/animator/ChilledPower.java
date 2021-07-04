package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class ChilledPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ChilledPower.class);

    private float percentage;

    public static float CalculatePercentage(int amount)
    {
        return 100f / (100f + 3 * amount);
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
        updateDescription();
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
        updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        updatePercentage();
        updateDescription();
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();
        GameActions.Bottom.ReducePower(this, Math.max(this.amount / 2,1));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return calculateDamageGiven(damage, 0);
    }

    public float calculateDamageGiven(float damage, float modifier)
    {
        return damage * (percentage + modifier);
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL)
        {
            damageAmount = Math.round(percentage * (float) damageAmount);
            info.output = damageAmount;
        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    private void updatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
    }
}

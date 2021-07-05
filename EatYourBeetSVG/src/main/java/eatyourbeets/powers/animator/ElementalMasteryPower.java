package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class ElementalMasteryPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ElementalMasteryPower.class);

    public float percentage;
    public static final float MULTIPLIER = 2;

    public static float CalculatePercentage(int amount)
    {
        return (100 + amount * MULTIPLIER) / 100f;
    }

    public ElementalMasteryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.BUFF;
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

    private void updatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
    }
}

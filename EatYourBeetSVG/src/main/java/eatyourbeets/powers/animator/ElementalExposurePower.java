package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class ElementalExposurePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ElementalExposurePower.class);
    public static final int DECAY_TURNS = 1;
    public int secondaryAmount;

    private float percentage;

    public static float CalculatePercentage(int amount)
    {
        return (100f + amount) / 100f;
    }

    public ElementalExposurePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        this.secondaryAmount = DECAY_TURNS;

        updatePercentage();
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0)
        {
            DecimalFormat df = new DecimalFormat("#.0");
            String value = df.format(((1 - this.percentage) * 100));

            this.description = FormatDescription(0, amount, secondaryAmount);
        }
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(secondaryAmount, Color.WHITE, c.a);
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
        this.secondaryAmount = DECAY_TURNS;
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

        if (this.secondaryAmount <= 0) {
            GameActions.Bottom.RemovePower(owner, owner, this);
        }
        else {
            this.secondaryAmount -= 1;
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        if (owner.hasPower(WeakPower.POWER_ID) && type == DamageInfo.DamageType.NORMAL)
        {
            float base = !owner.isPlayer && player.hasRelic("Paper Crane") ? 0.6F : 0.75F;
            float newDamage = calculateMultiplier(damage,base);
            return super.atDamageGive(newDamage, type);
        }
        return super.atDamageGive(damage, type);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (owner.hasPower(VulnerablePower.POWER_ID) && type == DamageInfo.DamageType.NORMAL) {
            float base = 1.5F;
            if (owner.isPlayer && player.hasRelic("Odd Mushroom")) {
                base = 1.25F;
            } else if (!owner.isPlayer && player.hasRelic("Paper Frog")){
                base = 1.75F;
            }
            float newDamage = calculateMultiplier(damage,base);
            return super.atDamageReceive(newDamage, type);
        }
        return damage;
    }

    private void updatePercentage()
    {
        percentage = CalculatePercentage(this.amount);
        updateDescription();
    }

    private int calculateMultiplier(float value, float baseMultiplier) {
        float modifiedMultiplier = (1.0f - baseMultiplier) * percentage;
        return MathUtils.ceil(value * (1 - modifiedMultiplier) / baseMultiplier);
    }
}

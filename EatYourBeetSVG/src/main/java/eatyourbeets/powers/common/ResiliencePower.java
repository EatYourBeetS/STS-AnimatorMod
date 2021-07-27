package eatyourbeets.powers.common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class ResiliencePower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(ResiliencePower.class);
    public static final int MULTIPLIER = 10;
    private float percentage;

    public static float CalculatePercentage(int amount)
    {
        return (100f - (amount * MULTIPLIER)) / 100f;
    }

    public ResiliencePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        if (amount > 0)
        {
            DecimalFormat df = new DecimalFormat("#.0");
            String value = df.format(((1 - this.percentage) * 100));

            this.description = FormatDescription(0, amount);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        GameActions.Bottom.GainTemporaryHP(amount);
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

    @Override
    public float modifyBlock(float blockAmount) {
        if (owner.hasPower(FrailPower.POWER_ID)) {
            float newBlock = calculateMultiplier(blockAmount,0.75F);
            return super.modifyBlock(newBlock);
        }
        return super.modifyBlock(blockAmount);
    }

    private int calculateMultiplier(float value, float baseMultiplier) {
        float modifiedMultiplier = (1.0f - baseMultiplier) * percentage;
        float finalMultiplier = Math.max(0, 1 - modifiedMultiplier);
        return MathUtils.ceil(value * finalMultiplier / baseMultiplier);
    }
}

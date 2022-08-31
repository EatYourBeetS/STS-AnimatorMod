package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

import java.text.DecimalFormat;

public class EnchantedArmorPlayerPower extends AnimatorPower
{
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.0");

    public static final String POWER_ID = CreateFullID(EnchantedArmorPlayerPower.class);
    public static final int MAX_REDUCTION = 50;

    public static float CalculatePercentage(int amount)
    {
        return (100 - Math.min(MAX_REDUCTION, 10 * amount)) / 100f;
    }

    public EnchantedArmorPlayerPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, Math.min(MAX_REDUCTION, amount * 10), MAX_REDUCTION);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            damage *= CalculatePercentage(amount);
        }

        return super.atDamageReceive(damage, type);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        final int block = Math.min(amount, owner.currentBlock);
        if (block > 0)
        {
            owner.loseBlock(block);
            GameActions.Bottom.GainTemporaryHP(block);
            flash();
        }
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(amount, Color.GOLD, c.a);
    }
}

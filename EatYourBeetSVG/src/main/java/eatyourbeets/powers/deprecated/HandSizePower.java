package eatyourbeets.powers.deprecated;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.powers.AnimatorPower;

public class HandSizePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HandSizePower.class);

    public HandSizePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount, amount > 0 ? PowerType.BUFF : PowerType.DEBUFF, false);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, BaseMod.MAX_HAND_SIZE);
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(BaseMod.MAX_HAND_SIZE), x, y, this.fontScale, c);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        onRemove();
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);

        BaseMod.MAX_HAND_SIZE += difference;

        if (amount >= BaseMod.DEFAULT_MAX_HAND_SIZE)
        {
            this.type = PowerType.BUFF;
        }
        else
        {
            this.type = PowerType.DEBUFF;
        }
    }
}